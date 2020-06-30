package com.raul.licenta.service

import com.raul.licenta.model.Role
import com.raul.licenta.model.User
import com.raul.licenta.model.UserDetails
import com.raul.licenta.model.UserRole
import com.raul.licenta.repository.ImageRepository
import com.raul.licenta.repository.UserRepository
import com.raul.licenta.repository.UserRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,

    @Autowired
    private val userRoleRepository: UserRoleRepository,

    @Autowired
    private val imageRepository: ImageRepository
) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun register(user: User) {
        try {
            userRepository.save(user)
        } catch (ex: Exception) {
            throw Exception("Could not save user")
        }
    }

    @Transactional
    fun changeRoles(id: Long, roles: List<Role>) {
        val user = userRepository.findById(id).orElseThrow{
            Exception("User not found")
        }
        userRoleRepository.deleteByUser(user)
        user.userRoles.clear()
        for (role in roles) {
            val userRole = UserRole(0, role)
            user.userRoles.add(userRole)
            userRole.user = user
        }
        try {
            userRepository.save(user)
        } catch (ex: Exception) {
            throw Exception("Could not save user")
        }
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.deleteById(id)
    }

    fun currentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal as? UserDetails ?:
            throw Exception("No current user")
        return userRepository.findById(principal.data.id).orElseThrow {
            Exception("No current user")
        }
    }

    fun getOwnerOfImage(id: Long) = imageRepository.findById(id).orElseThrow { Exception("Image not found") }.user
}