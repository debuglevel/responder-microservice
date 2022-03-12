package de.debuglevel.responder

import java.util.*

object AuthenticationUtils {
    fun getBasicAuthenticationHeader(): String {
        val encodedCredentials =
            Base64.getEncoder().encodeToString("SECRET_USERNAME:SECRET_PASSWORD".byteInputStream().readBytes())
        val basicAuthenticationHeader = "Basic $encodedCredentials"
        return basicAuthenticationHeader
    }
}