package com.raul.licenta.model

import com.fasterxml.jackson.annotation.JsonValue

enum class  ExceptionMessage(val message: String = toString()) {
    Unknown("UnknownError"),
    JWTInvalidFormat("JWT has invalid format!"),
    NoUserWithThatUsername("No user with the provided username was found!"),
    NoUserWithThatId("No user with the provided id was found!"),
    NoImageWithThatId("No image with the provided id was found!"),
    NoCurrentUser("No user was found based on the authentication!"),
    UploadCorrupted("Upload failed due to corrupted data!"),
    ImageExists("There already is an image like this in the database"),
    ImageWithNameExists("There already is an image with this name in the database"),
    UserAlreadyRegistered("There is already an user with this username!"),
    WrongPassword("Bad credentials!"),
    UserNotActivated("User not activated!"),
    AuthorizationCodeExpired("Authorization code expired!");

    @JsonValue
    open fun toValue(): Int {
        return ordinal
    }
}