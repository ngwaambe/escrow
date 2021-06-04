package com.sicuro.escrow.service

import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.model.CompleteSignupRequest
import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.persistence.ActivationLinkRepository
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import com.sicuro.escrow.util.MessageBundleKey
import com.sicuro.escrow.util.MessageUtil
import com.sicuro.escrow.util.TextHelper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.mail.MessagingException

@Service
class SignupService @Autowired constructor(
    val customerRepository: CustomerRepository,
    val activationLinkRepository: ActivationLinkRepository,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val mailService: MailService,
    @Value("\${host.address}") val hostName: String){

    @Transactional
    fun signup(signupRequest: SignupRequest) {
        userRepository.findByUsername(signupRequest.contact.email)?.let{
            throw UserAlreadyExistException("User already exist exception")
        }
        val customer = customerRepository.createCustomer(signupRequest)
        val encodedPassword = passwordEncoder.encode(signupRequest.contact.password)

        val (user, activationLinkId) = userRepository.createUser(
            signupRequest.contact.email,
            encodedPassword,
            customer.id!!,
            listOf("ROLE_CUSTOMER")
        )
        val props = mutableMapOf<String, String>()
        props["name"] = TextHelper.getName(customer)
        props["link"] = createRegistrationLink(activationLinkId)
        props["accountNumber"] = customer.customerNumber
        props["userName"] = user.username
        val subject: String = getSubject(customer.language,"registration.checkDataText")!!
        val email: String = customer.email
        val template: String = getMailTemplate(customer.language,"registrationActivation")
        sendMail(email, subject, template,  props)
    }

    @Transactional
    fun completeSignup(customerId: Long, completSignupRequest: CompleteSignupRequest) {
        val customer = customerRepository.getCustomer(customerId)
        customerRepository.updateAddress(completSignupRequest.address, customer, true)
        userRepository.updateUserSequrityQuestion(completSignupRequest.securityQuestion, customer.email)
    }

    @Transactional
    fun activateAccount(activationUuid: String) {
        logger.info("activating account for activationId:${activationUuid}")
        val link = activationLinkRepository.findByIdAndType(activationUuid, LinkType.ACCOUNT_ACTIVATION)
        userRepository.activateUser(link.user.username)
        activationLinkRepository.delete(activationUuid)
        logger.info("account :${link.user.username} has been activated")
     }

    @Transactional
    fun initiatePasswordReset(email: String) {
        val linkId  = userRepository.initiateResetPassword(email)
        val customer = customerRepository.getCustomerByEmail(email)

        val props = mutableMapOf<String, String>()
        props["name"] = TextHelper.getName(customer)
        props["link"] = createResetpasswordLink(linkId)
        props["accountNumber"] = customer.customerNumber
        props["userName"] = customer.email
        val subject: String = getSubject(customer.language, "ResetpasswordStepOne")!!
        val email: String = customer.email
        val template: String = getMailTemplate(customer.language,"initiateResetPassword")
        sendMail(email, subject, template, props)
    }

    fun resetPassword(activationUuid: String) {
        val link = activationLinkRepository.findByIdAndType(activationUuid, LinkType.RESET_PASSWORD)
        val newpassword = userRepository.resetPassword(link.user.username)
        val customer = customerRepository.getCustomerByEmail(link.user.username)

        val props = mutableMapOf<String, String>()
        props["name"] = TextHelper.getName(customer)
        props["link"] = StringBuilder().append(hostName).append("/authenticate").toString()
        props["accountNumber"] = customer.customerNumber
        props["userName"] = customer.email
        props["password"] = newpassword
        val subject: String = getSubject(customer.language, "ResetPasswordRequest")!!
        val email: String = customer.email
        val template: String = getMailTemplate(customer.language, "resetPassword")
        sendMail(email, subject, template, props)
    }

    private fun sendMail(email:String, subject: String, template: String, props:MutableMap<String, String>) {
        try {
            mailService.sendMail(email, subject, template, props)
        }catch (e: Exception) {
            when(e) {
                is MessagingException,  is MailException ->{
                    e.printStackTrace()
                    logger.error(e.localizedMessage)
                    throw SendMailException("Fail sending registration mail", e)
                }
                else ->{ throw e}
            }
        }
    }

    private fun getSubject(locale:String, label:String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage(label)

    private fun createRegistrationLink(activationUuid: String): String {
        return StringBuilder()
            .append(hostName)
            .append("/activateaccount?uuid=")
            .append(activationUuid)
            .toString()
    }

    private fun createResetpasswordLink(activationUuid: String): String {
        return StringBuilder()
            .append(hostName)
            .append("/resetpassword?uuid=")
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
