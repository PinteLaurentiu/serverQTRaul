package com.raul.licenta.exception

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception

@JsonSerialize(using = Oauth2ExceptionDecoratorSerializer::class)
class Oauth2ExceptionDecorator(val target: ServerBaseException) : OAuth2Exception(target.message, target)

