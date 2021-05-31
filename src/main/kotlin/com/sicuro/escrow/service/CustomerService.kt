package com.sicuro.escrow.service

import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.model.*
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
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val mailService: MailService,
    @Value("\${host.address}") val hostName: String) {

    fun getCustomers(filter:CustomerFilterRequest) = customerRepository.getCustomers(filter)

    fun getCustomer(customerId:Long) = customerRepository.getCustomer(customerId)

    @Transactional
    fun createCustomer(request: CustomerCreateRequest) {
        userRepository.findByUsername(request.contact.email)?.let{
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
    }

    fun updateCustomer(customerId:Long, request: CustomerDetailRequest):Customer {
        val customer = customerRepository.getCustomer(customerId);

        val updatedCustomer = customer.copy(
            title = request.title,
            language = request.language,
            firstName = request.firstName,
            lastName = request.lastName,
            organisation = request.organisation?.name ?: customer.organisation,
            taxNumber = request.organisation?.taxNumber ?: customer.taxNumber
        )
        return customerRepository.updateCustomerDetails(updatedCustomer)
    }

    fun changePassword(customerId:Long, request: ChangePasswordRequest) {
        val customer = customerRepository.getCustomer(customerId)
        userRepository.changePassword(customer.email, request.password)
    }

    @Transactional
    fun changeEmail(customerId:Long, request: ChangeEmailRequest) {
        val customer = customerRepository.getCustomer(customerId)
        userRepository.changePassword(customer.email, request.email)
        customerRepository.changeEmail(customerId, request.email)
    }

    private fun sendMail(customer: Customer, uuid: String, password:String) {
        try {
            val model: MutableMap<String, String> = HashMap()
            model["name"] = TextHelper.getName(customer)
            model["link"] = createRegistrationLink(uuid)
            model["accountNumber"] = customer.customerNumber
            model["userName"] = customer.email
            val subject: String = getSubject(customer.language)!!
            val email: String = customer.email
            val template: String = getMailTemplate(customer.language,"accountCreation")
            mailService.sendMail(email, subject, template, model)
        } catch (e: Exception) {
            when(e) {
                is MessagingException,  is MailException ->{
                    SignupService.logger.error(e.localizedMessage)
                    throw SendMailException("Fail sending registration mail", e)
                }
                else ->{ throw e}
            }
        }
    }

    private fun getSubject(locale:String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage("registration.checkDataText")

    private fun createRegistrationLink(activationUuid: String): String {
        return StringBuilder()
            .append(hostName)
            .append("/activateaccount.jsf?uuid=")
            .append(activationUuid)
            .toString()
    }

    private fun getMailTemplate(locale:String, templateName:String)= "/"+locale+"/"+templateName+".ftl"

    private fun messageUtil (locale:String, resource:String) : MessageUtil {
        return MessageUtil.getInstance(MessageBundleKey(Locale(locale), resource))
    }

}