package com.sicuro.escrow.service

import com.sicuro.escrow.exception.ExpiredAccountActivationLink
import com.sicuro.escrow.exception.InvalidActivationLinkException
import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.util.MessageBundleKey
import com.sicuro.escrow.util.MessageUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.*
import javax.mail.MessagingException
import kotlin.collections.HashMap

@Service
class SignupService @Autowired constructor(
    val customerRepository: CustomerRepository,
    val userDao: UserDao,
    val activationLinkDao: ActivationLinkDao,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val mailService: MailService,
    @Value("\${host.address}") val hostName: String,
    @Value("\${activationlink.expiration}") val expirationRange: Long){

    @Transactional
    fun signup(signupRequest: SignupRequest) {
        userDao.findByUsername(signupRequest.contact.email)?.let{
            throw UserAlreadyExistException("User already exist exception")
        }
        val (customer, activationId) = customerRepository.createCustomer(signupRequest)
        val encodedPassword = passwordEncoder.encode(signupRequest.contact.password)
        userRepository.createUser(
            signupRequest.contact.email,
            encodedPassword,
            listOf("ROLE_CUSTOMER")
        )

        sendMail(customer, activationId)
    }

    @Transactional
    fun activateAccount(activationUuid: String) {
        val link = activationLinkDao.findById(activationUuid).orElseThrow{
            InvalidActivationLinkException("Unknown activation link")
        }

        if (link.created!!.isAfter(OffsetDateTime.now().minusHours(expirationRange))) {
            throw ExpiredAccountActivationLink("Your activation link has expired")
        }

        userDao.findByUsername(link.customer.email)?.also { user ->
            user.status = BaseStatus.active
            userDao.save(user)
        }?: throw RuntimeException("Could not resolve user in validated activation link")
    }

    private fun sendMail(customer: Customer, uuid: String) {
        try {
            val model: MutableMap<String, String> = HashMap()
            model["name"] = getName(customer)
            model["link"] = createRegistrationLink(uuid)
            model["accountNumber"] = customer.customerNumber
            model["userName"] = customer.email
            val subject: String = getSubject(customer.preferedLanguage)!!
            val email: String = customer.email
            val template: String = getMailTemplate(customer.preferedLanguage,"registrationActivation")
            mailService.sendMail(email, subject, template, model)
        } catch (e: Exception) {
            when(e) {
                is MessagingException,  is MailException ->{
                    logger.error(e.localizedMessage)
                    throw SendMailException("Fail sending registration mail", e)
                }
                else ->{ throw e}
            }
        }
    }

    private fun getName(customer: Customer):String {
        val name = StringBuilder()

        getLocalizedTitle(customer.preferedLanguage, customer.title.elKey)?.let {
            name.append(it)
        } ?: name.append(customer.title.name)

        return name.append(" ")
            .append(customer.firstName)
            .append(" ")
            .append(customer.lastName)
            .toString()
    }

    private fun getSubject(locale:String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage("registration.checkDataText")

    private fun getLocalizedTitle(locale:String, titleKey:String):String? = messageUtil(locale, "com.sicuro.i18n.application").getMessage(titleKey)

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

    companion object {
        val logger = LoggerFactory.getLogger(SignupService::class.java)
    }

}
