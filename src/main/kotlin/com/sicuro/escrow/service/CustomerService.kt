package com.sicuro.escrow.service

import com.sicuro.escrow.exception.InvalidCurrentPasswordException
import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.model.*
import com.sicuro.escrow.persistence.PaymentAccountRepository
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import com.sicuro.escrow.util.MessageBundleKey
import com.sicuro.escrow.util.MessageUtil
import com.sicuro.escrow.util.TextHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.mail.MessagingException
import kotlin.collections.HashMap

@Service
class CustomerService @Autowired constructor(
    val customerRepository: CustomerRepository,
    val paymentAccountRepository: PaymentAccountRepository,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val mailService: MailService,
    @Value("\${frontend.host.base_url}") val frontendHostBaseUrl: String
) {

    fun getCustomers(filter: CustomerFilter) = customerRepository.getCustomers(filter)

    fun getCustomer(customerId: Long) = customerRepository.getCustomer(customerId)

    @Transactional
    fun createCustomer(request: CreateCustomer): Customer {
        userRepository.findByUsername(request.contact.email)?.let {
            throw UserAlreadyExistException("User already exist exception")
        }
        val customer = customerRepository.createCustomer(request)
        val encodedPassword = passwordEncoder.encode(request.contact.password)
        val (_, linkId) = userRepository.createUser(
            request.contact.email,
            encodedPassword,
            customer.id!!,
            request.roles,
            BaseStatus.active
        )
        sendMail(customer, linkId, request.contact.password)
        return customer
    }

    fun updateCustomer(customerId: Long, request: CustomerDetail): Customer {
        val customer = customerRepository.getCustomer(customerId)

        val updatedCustomer = customer.copy(
            title = request.title,
            language = request.language,
            firstname = request.firstname,
            lastname = request.lastname,
            organisation = request.organisation?.name ?: customer.organisation,
            taxNumber = request.organisation?.taxNumber ?: customer.taxNumber
        )
        return customerRepository.updateCustomerDetails(updatedCustomer)
    }

    fun updateCustomerAddress(customerId: Long, request: Address) = customerRepository.updateAddress(customerId, request)

    fun changePassword(customerId: Long, request: ChangePassword) {
        customerRepository.getCustomer(customerId)?.let {
            if (userRepository.validatePassword(it.email, request.currentPassword)) {
                userRepository.changePassword(it.email, request.password)
            } else throw InvalidCurrentPasswordException("Current password is not correct")
        }
    }

    @Transactional
    fun changeEmail(customerId: Long, request: ChangeEmail) {
        val customer = customerRepository.getCustomer(customerId)
        userRepository.changePassword(customer.email, request.email)
        customerRepository.changeEmail(customerId, request.email)
    }

    fun addPaymentAccount(customerId: Long, paymentAccount: PaymentAccount) = paymentAccountRepository.add(customerId, paymentAccount)

    fun setDefaultAccount(customerId: Long, paymentAccountId: Long) = paymentAccountRepository.setDefaultCustomerAccount(customerId, paymentAccountId)

    private fun sendMail(customer: Customer, uuid: String, password: String) {
        try {
            val model: MutableMap<String, String> = HashMap()
            model["name"] = TextHelper.getName(customer)
            model["link"] = createRegistrationLink(uuid)
            model["accountNumber"] = customer.customerNumber
            model["userName"] = customer.email
            model["password"] = password
            val subject: String = getSubject(customer.language)!!
            val email: String = customer.email
            val template: String = getMailTemplate(customer.language, "accountCreation")
            mailService.sendMail(email, subject, template, model)
        } catch (e: Exception) {
            when (e) {
                is MessagingException, is MailException -> {
                    SignupService.logger.error(e.localizedMessage)
                    throw SendMailException("Fail sending registration mail", e)
                }
                else -> { throw e }
            }
        }
    }

    private fun getSubject(locale: String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage("registration.checkDataText")

    private fun createRegistrationLink(activationUuid: String): String {
        return StringBuilder()
            .append(frontendHostBaseUrl)
            .append("activateaccount.jsf?uuid=")
            .append(activationUuid)
            .toString()
    }

    private fun getMailTemplate(locale: String, templateName: String) = "/" + locale + "/" + templateName + ".ftl"

    private fun messageUtil(locale: String, resource: String): MessageUtil {
        return MessageUtil.getInstance(MessageBundleKey(Locale(locale), resource))
    }
}
