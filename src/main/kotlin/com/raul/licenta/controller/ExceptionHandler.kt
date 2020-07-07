package com.raul.licenta.controller

import com.raul.licenta.exception.ServerBaseException
import com.raul.licenta.model.ErrorResponseStatus
import com.raul.licenta.model.ExceptionMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.reflect.UndeclaredThrowableException


@RestControllerAdvice(annotations = [RestController::class])
class ExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = [Throwable::class])
    fun handleException(throwable: Throwable): ResponseEntity<*> {
        val exception = if (throwable is UndeclaredThrowableException) throwable.undeclaredThrowable else throwable
        if (exception is ServerBaseException) {
            return ResponseEntity(ErrorResponseStatus(exception.message.orEmpty(), exception.baseMessage),
                    exception.httpStatus)
        }
        return ResponseEntity(ErrorResponseStatus(exception.message ?: ExceptionMessage.Unknown.message,
                    ExceptionMessage.Unknown), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
