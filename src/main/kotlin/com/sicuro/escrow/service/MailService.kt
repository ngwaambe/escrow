package com.sicuro.escrow.service

import freemarker.template.TemplateException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig
import java.io.IOException
import java.util.*
import javax.mail.MessagingException

@Service
class MailService @Autowired constructor(
    val freeMarkerConfiguration: FreeMarkerConfig,
    val javaMailSender: JavaMailSender,
    @Value("\${mail.sender.address}") val mailInfoAddress: String
){
    /**
     * sender email address property key.
     */
    private val MAIL_SENDER_KEY = "mail.info.address"

    /**
     * Default locale.
     */
    private val DEFAULT_LOCALE = "en"

    /**
     * Logger
     */
    companion object {
        private val logger = LoggerFactory.getLogger(MailService::class.java)
    }


    @Throws(MailException::class, MessagingException::class)
    fun sendMail(emailAddress: String, subject: String, template: String, properties: Map<String, String>) {
        val emails: MutableList<String> = ArrayList()
        emails.add(emailAddress)
        val preparator: MimeMessagePreparator = createMessagePreparator(emails, subject, template, properties, null)
        javaMailSender.send(preparator)
    }

    @Throws(MailException::class, MessagingException::class)
    fun sendMail(emailAddress: String, subject: String, template: String, properties: Map<String, String>,
                 locale: String) {
        val actualTemplate = getMailTemplate(template, locale)
        logger.info("EmailTemplate-PATH:{}", actualTemplate)
        sendMail(emailAddress, subject, actualTemplate, properties)
    }

    /**
     * Based on user prefered language the appropriet template is choosen. In case the prefered language is not
     * available the current locale language is used.
     *
     * @param templateName
     * @return
     */
    private fun getMailTemplate(templateName: String, locale: String): String {
        return if (templateName.endsWith("ftl")) {
            "/$locale/$templateName"
        } else "/$locale/$templateName.ftl"
    }



    @Throws(MailException::class, MessagingException::class)
    fun sendMail(emailAddress: String, subject: String, template: String, properties: Map<String, String>, locale: String, attachementResource: FileSystemResource?) {
        val actualTemplate = getMailTemplate(template, locale)
        val emails: MutableList<String> = ArrayList()
        emails.add(emailAddress)
        val preparator: MimeMessagePreparator = createMessagePreparator(emails, subject, actualTemplate, properties, attachementResource)
        javaMailSender.send(preparator)
    }

    @Throws(MailException::class, MessagingException::class)
    fun sendMail(emailAddress: List<String>, subject: String, template: String, properties: Map<String, String>, locale: String, attachementResource: FileSystemResource?) {
        val actualTemplate = getMailTemplate(template, locale)
        val preparator: MimeMessagePreparator = createMessagePreparator(emailAddress, subject, actualTemplate, properties, attachementResource)
        javaMailSender.send(preparator)
    }

    private fun createMessagePreparator(emailAddress: List<String>, subject: String, template: String, properties: Map<String, String>, attachementResource: FileSystemResource?): MimeMessagePreparator {
        return MimeMessagePreparator { mm ->
            val mimeMessage = MimeMessageHelper(mm, true, "UTF-8")
            mimeMessage.setFrom(mailInfoAddress)
            for (email in emailAddress) {
                mimeMessage.addTo(email)
            }
            mimeMessage.setSubject(subject)
            val configuration = freeMarkerConfiguration.configuration
            try {
                //getFreemarkerMailConfiguration().setClassForTemplateLoading(this.getClass(), "");
                val templateAsString =  configuration.getTemplate(template)
                val resultTxt = FreeMarkerTemplateUtils.processTemplateIntoString(templateAsString, properties)
                val resultHtml = FreeMarkerTemplateUtils.processTemplateIntoString(templateAsString, properties)
                mimeMessage.setText(resultTxt, resultHtml)
                if (attachementResource != null) {
                    mimeMessage.addAttachment(attachementResource.filename, attachementResource)
                }
            } catch (e: IOException) {
                val error = "IO error while trying to access email templates"
                logger.error(error, e)
                throw MessagingException(error, e)
            } catch (e: TemplateException) {
                val error = "Error while processing mail templates"
                logger.error(error, e)
                throw MessagingException(error, e)
            }
        }
    }

}
