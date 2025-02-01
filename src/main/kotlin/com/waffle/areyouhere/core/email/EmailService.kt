package com.waffle.areyouhere.core.email

import com.waffle.areyouhere.core.email.model.MessageHolder
import com.waffle.areyouhere.core.email.model.MessageTemplate
import com.waffle.areyouhere.util.EnvironmentService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

// FIXME: non-blocking 구현
@Service
class EmailService(
    @Value("\${spring.mail.from}") private val from: String,
    @Value("\${spring.mail.username}") private val username: String,
    @Value("\${spring.mail.password}") private val password: String,
    @Value("\${spring.mail.host}") private val host: String,
    @Value("\${spring.mail.port}") private val port: Int,
    private val environmentService: EnvironmentService,
) {
    private val logger = KotlinLogging.logger { }

    suspend fun sendVerifyEmail(to: String, verificationLink: String, messageTemplate: MessageTemplate) {
        val title = "이메일 인증"
        val messageHolder = MessageHolder(title, messageTemplate, verificationLink)
        sendSimpleMessage(to, title, messageHolder.contents)
    }

    suspend fun sendSimpleMessage(to: String, title: String, content: String) {
        withContext(Dispatchers.IO) {
            if (environmentService.isLocal()) {
                logger.info { "Email text: " + content }
                return@withContext
            }
            val session = getSession()
            val msg = setMessage(to, title, content, session)
            doSend(session, msg)
        }
    }

    private suspend fun setMessage(to: String, title: String, content: String, session: Session): MimeMessage {
        val msg = MimeMessage(session)
        msg.setFrom(InternetAddress(from, from))
        val internetAddress = InternetAddress(to)
        internetAddress.validate()
        msg.setRecipient(Message.RecipientType.TO, internetAddress)
        msg.subject = title
        msg.setContent(content, "text/html")
        msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet")
        return msg
    }

    private suspend fun doSend(session: Session, msg: MimeMessage) {
        val transport = session.getTransport()
        transport.connect(host, username, password)
        transport.sendMessage(msg, msg.allRecipients)
        transport.close()
    }

    private suspend fun getSession(): Session {
        val emailProps = System.getProperties()
        emailProps["mail.transport.protocol"] = "smtp"
        emailProps["mail.smtp.port"] = port
        emailProps["mail.smtp.starttls.required"] = "true"
        emailProps["mail.smtp.auth.login.disable"] = "true"
        emailProps["mail.smtp.starttls.enable"] = "true"
        emailProps["mail.smtp.auth"] = "true"
        return Session.getDefaultInstance(emailProps)
    }
}
