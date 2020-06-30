package com.raul.licenta.dto

import com.raul.licenta.model.Role

data class UserDto(
    val id: Long = 0,
    val name: String = "",
    val username: String = "",
    val roles: List<Role> = emptyList()
)