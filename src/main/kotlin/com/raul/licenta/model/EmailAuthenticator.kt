package com.raul.licenta.model

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.internet.InternetAddress

object EmailAuthenticator : Authenticator() {
    private const val username = "raulapp.noreply@gmail.com"
    private const val password = "123456-raul"
    val address : InternetAddress get() = InternetAddress(username)
    override fun getPasswordAuthentication(): PasswordAuthentication = PasswordAuthentication(username, password)
}