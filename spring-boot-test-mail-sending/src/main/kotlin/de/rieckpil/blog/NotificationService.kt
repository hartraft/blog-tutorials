package de.rieckpil.blog

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class NotificationService(private val javaMailSender: JavaMailSender) {
    fun notifyUser(email: String?, content: String?) {
        val mail = SimpleMailMessage()
        mail.from = "admin@spring.io"
        mail.subject = "A new message for you"
        mail.text = content
        mail.setTo(email)
        javaMailSender.send(mail)
    }
}