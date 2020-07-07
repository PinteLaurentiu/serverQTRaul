package com.raul.licenta.service

import com.raul.licenta.exception.ServerBaseException
import com.raul.licenta.model.*
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
        if (userRepository.findByUsername(user.username).isPresent) {
            throw ServerBaseException(ExceptionMessage.UserAlreadyRegistered)
        }
        userRepository.save(user)
    }

    @Transactional
    fun changeRoles(id: Long, roles: List<Role>) {
        val user = userRepository.findById(id).orElseThrow{
            ServerBaseException(ExceptionMessage.NoUserWithThatId)
        }
        userRoleRepository.deleteByUser(user)
        user.userRoles.clear()
        for (role in roles) {
            val userRole = UserRole(0, role)
            user.userRoles.add(userRole)
            userRole.user = user
        }
        userRepository.save(user)
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.findById(id).orElseThrow{
            ServerBaseException(ExceptionMessage.NoUserWithThatId)
        }
        userRepository.deleteById(id)
    }

    fun currentUser(): User {
        val principal = SecurityContextHolder.getContext().authentication.principal as? UserDetails ?:
            throw ServerBaseException(ExceptionMessage.NoCurrentUser)
        return userRepository.findById(principal.data.id).orElseThrow {
            ServerBaseException(ExceptionMessage.NoCurrentUser)
        }
    }

    fun getOwnerOfImage(id: Long) = imageRepository.findById(id)
            .orElseThrow{ ServerBaseException(ExceptionMessage.NoImageWithThatId) }.user
}