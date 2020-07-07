package com.raul.licenta.model

class ErrorResponseStatus(var message: String,
                          var code: ExceptionMessage,
                          var success: Boolean = false)
