package com.raul.licenta.exception

import com.raul.licenta.model.ExceptionMessage
import org.springframework.http.HttpStatus

open class ServerBaseException(
        val baseMessage: ExceptionMessage = ExceptionMessage.Unknown,
        val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String = baseMessage.message
    ) : Exception(message)