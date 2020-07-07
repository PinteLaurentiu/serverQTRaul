package com.raul.licenta.configuration

import com.raul.licenta.exception.Oauth2ExceptionDecorator
import com.raul.licenta.exception.ServerBaseException
import com.raul.licenta.model.ExceptionMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator
import java.lang.Exception
import java.lang.reflect.UndeclaredThrowableException

class ExceptionTranslator : DefaultWebResponseExceptionTranslator() {
    override fun translate(e: Exception): ResponseEntity<OAuth2Exception> {
        val responseEntity = super.translate(e)
        val exception = if (responseEntity.hasBody()) translateToBaseException(responseEntity.body!!)
                        else ServerBaseException(ExceptionMessage.Unknown, HttpStatus.UNAUTHORIZED)

        return ResponseEntity(Oauth2ExceptionDecorator(exception), exception.httpStatus)
    }

    private fun translateToBaseException(exception: OAuth2Exception): ServerBaseException {
        val baseException = extractBaseException(exception)
        if (baseException != null) {
            return baseException
        }
        if (exception.message == "Bad credentials") {
            return ServerBaseException(ExceptionMessage.WrongPassword, HttpStatus.UNAUTHORIZED)
        }
        if (exception.message == "User is disabled") {
            return ServerBaseException(ExceptionMessage.UserNotActivated, HttpStatus.UNAUTHORIZED)
        }
        if (exception.message?.startsWith("Invalid authorization baseMessage: ") == true) {
            return ServerBaseException(ExceptionMessage.AuthorizationCodeExpired, HttpStatus.UNAUTHORIZED)
        }
        return ServerBaseException(ExceptionMessage.Unknown, HttpStatus.UNAUTHORIZED)
    }

    private fun extractBaseException(body: OAuth2Exception): ServerBaseException? {
        val cause1 = body.cause as? InternalAuthenticationServiceException ?: return null
        val cause2 = cause1.cause as? UndeclaredThrowableException ?: return null
        return cause2.undeclaredThrowable as? ServerBaseException ?: return null
    }
}