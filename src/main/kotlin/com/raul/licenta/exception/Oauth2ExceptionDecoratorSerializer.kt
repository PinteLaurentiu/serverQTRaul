package com.raul.licenta.exception

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.raul.licenta.model.ErrorResponseStatus

class Oauth2ExceptionDecoratorSerializer :
    StdSerializer<Oauth2ExceptionDecorator>(Oauth2ExceptionDecorator::class.java) {
    override fun serialize(value: Oauth2ExceptionDecorator, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeObject(ErrorResponseStatus(value.target.message.orEmpty(), value.target.baseMessage))
    }
}