package com.sicuro.escrow.service

import com.sicuro.escrow.exception.*
import com.sicuro.escrow.model.*
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
import java.time.OffsetDateTime
import java.util.*
import javax.mail.MessagingException

@Service
class SignupService @Autowired constructor(
    val customerRepository: CustomerRepository,
    val activationLinkRepository: ActivationLinkRepository,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val mailService: MailService,
    @Value("\${frontend.host.base_url}") val hostName: String,
    @Value("\${activationlink.expiration.resetpassword}") val resetPasswordExpiringOffset: Long
) {

    @Transactional
    fun signup(signupRequest: SignupRequest) {
        userRepository.findByUsername(signupRequest.contact.email)?.let {
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
        val subject: String = getSubject(customer.language, "registration.checkDataText")!!
        val email: String = customer.email
        val template: String = getMailTemplate(customer.language, "registrationActivation")
        sendMail(email, subject, template, props)
    }

    @Transactional
    fun completeSignup(customerId: Long, completSignupRequest: CompleteSignupRequest) {
        val customer = customerRepository.updateAddress(customerId, completSignupRequest.address)
        val updateCustomer = customerRepository.resolveCustomerVat(customer)
        userRepository.updateUserSequrityQuestion(completSignupRequest.securityQuestion, updateCustomer.email)
    }

    @Transactional
    fun activateAccount(activationUuid: String) {
        logger.info("activating account for activationId:$activationUuid")
        activationLinkRepository.findByIdAndType(activationUuid, LinkType.ACCOUNT_ACTIVATION)?.let {
            if (it.active) {
                userRepository.activateUser(it.user.username)
                activationLinkRepository.save(it.copy(active = false))
                logger.info("account :${it.user.username} has been activated")
            } else {
                throw ResourceAlreadyUsedException("Activation link has already been used")
            }
        }
    }

    @Transactional
    fun initiatePasswordReset(email: String) {

        val customer = customerRepository.getCustomerByEmail(email)
        logger.info("Initiating password reset for customerId: {}", customer.id)

        val linkId = userRepository.initiateResetPassword(email)

        val props = mutableMapOf<String, String>()
        props["name"] = TextHelper.getName(customer)
        props["link"] = createResetpasswordLink(linkId)
        props["accountNumber"] = customer.customerNumber
        props["userName"] = customer.email
        val subject: String = getSubject(customer.language, "ResetpasswordStepOne")!!
        val email: String = customer.email
        val template: String = getMailTemplate(customer.language, "initiateResetPassword")
        sendMail(email, subject, template, props)
    }

    fun getSecurityQuestion(activationUuid: String): SecurityQuestionResponse {
        logger.info("Fetching secuityQuestion resetpassword activationId:{}", activationUuid)
        return activationLinkRepository.findByIdAndType(activationUuid, LinkType.RESET_PASSWORD)?.let {
            if (it.created!!.isAfter(OffsetDateTime.now().plusMinutes(resetPasswordExpiringOffset))) {
                logger.info("Resetpassword activationId:{} has expired", activationUuid)
                throw ExpiredLinkException("This lick is has expired")
            }
            it.user.securityQuestion?.let { question ->
                SecurityQuestionResponse(question, activationUuid)
            } ?: run {
                logger.info("Resetpassword activationId:{} could not be resolved", activationUuid)
                throw InvalidResourceException("Account")
            }
        }
    }

    fun resetPassword(request: ResetPasswordRequest) {
        val link = activationLinkRepository.findByIdAndType(request.activationId, LinkType.RESET_PASSWORD)
        if (passwordEncoder.matches(request.questionAnswer, link.user.securityQuestionAnswer)) {
            logger.info("reseting password for acivation link: {} userId: {}", request.activationId, link.user.id)
            val password = userRepository.resetPassword(link.user.username)
            val customer = customerRepository.getCustomerByEmail(link.user.username)

            val props = mutableMapOf<String, String>()
            props["name"] = TextHelper.getName(customer)
            props["link"] = StringBuilder().append(hostName).append("/authenticate").toString()
            props["accountNumber"] = customer.customerNumber
            props["userName"] = customer.email
            props["password"] = password
            val subject: String = getSubject(customer.language, "ResetPasswordRequest")!!
            val email: String = customer.email
            val template: String = getMailTemplate(customer.language, "resetPassword")
            sendMail(email, subject, template, props)
            activationLinkRepository.delete(link.uuid)
        } else throw InvalidSecurityQuestionAnswerException("Answer is not valid")
    }

    private fun sendMail(email: String, subject: String, template: String, props: MutableMap<String, String>) {
        try {
            mailService.sendMail(email, subject, template, props)
        } catch (e: Exception) {
            when (e) {
                is MessagingException, is MailException -> {
                    logger.error(e.localizedMessage)
                    throw SendMailException("Fail sending registration mail", e)
                }
                else -> {
                    throw e
                }
            }
        }
    }

    private fun getSubject(locale: String, label: String) = messageUtil(locale, "com.sicuro.i18n.application").getMessage(label)

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
            .append("/reset_password?uuid=")
            .append(activationUuid)
            .toString()
    }

    private fun getMailTemplate(locale: String, templateName: String) = "/" + locale + "/" + templateName + ".ftl"

    private fun messageUtil(locale: String, resource: String): MessageUtil {
        return MessageUtil.getInstance(MessageBundleKey(Locale(locale), resource))
    }

    companion object {
        val logger = LoggerFactory.getLogger(SignupService::class.java)
    }
}
