package com.raul.licenta.mapper

import com.raul.licenta.dto.OwnerDto
import com.raul.licenta.dto.RegisterDto
import com.raul.licenta.dto.UserDto
import com.raul.licenta.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserMapper(
        @Autowired
        private val userPasswordEncoder: PasswordEncoder
) {
    fun getUser(dto: RegisterDto): User {
        val user = User(0,
                userPasswordEncoder.encode(dto.password),
                dto.email,
                dto.name,
                hashSetOf(UserRole(0, Role.Unassigned)),
                mutableSetOf(),
                Activation(0, UUID.randomUUID().toString(), false))
        user.userRoles.first().user = user
        user.activation.user = user
        return user
    }

    fun getUser(model: User): UserDto = UserDto(model.id,
            model.name,
            model.username,
            model.userRoles.map { it.role })

    fun getUsers(allUsers: List<User>): List<UserDto> = allUsers.map { getUser(it) }

    fun getOwner(user: User) = OwnerDto(user.id, user.name)
}
