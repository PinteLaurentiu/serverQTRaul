package com.raul.licenta.service

import com.raul.licenta.model.Email
import com.raul.licenta.model.EmailAuthenticator
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.*

import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
class EmailService {
    private val properties by lazy {
        val properties = Properties()
        properties["mail.smtp.auth"] = true
        properties["mail.smtp.starttls.enable"] = true
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = 587
        properties["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        properties
    }
    private val timeout = 30L
    private val mimeType = "text/html"

    fun send(to: String, email: Email) {
        createThreadToWait{ sendRun(to, email) }
    }

    private fun sendRun(to: String, email: Email) {
        try {
            trySend(to, email)
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    @Throws(MessagingException::class)
    private fun trySend(to: String, email: Email) {
        val message = MimeMessage(createNewSession(EmailAuthenticator))
        message.setFrom(EmailAuthenticator.address)
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
        message.subject = email.subject
        message.setContent(email.content, mimeType)
        Transport.send(message)
    }

    private fun createNewSession(authenticator: Authenticator): Session {
        return Session.getInstance(properties, authenticator)
    }

    private fun createThreadToWait(runnable: ()->Unit) {
        Thread {
            try {
                createThreadAndWait(runnable)
            } catch (ignored: Exception) {
            }
        }.start()
    }

    @Throws(InterruptedException::class)
    private fun createThreadAndWait(runnable: ()->Unit) {
        val thread = Thread(runnable)
        thread.start()
        thread.join(1000 * timeout)
        if (thread.isAlive)
            thread.interrupt()
    }
}
