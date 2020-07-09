package com.raul.licenta.service

import com.raul.licenta.exception.ServerBaseException
import com.raul.licenta.model.*
import com.raul.licenta.repository.ActivationRepository
import com.raul.licenta.repository.ImageRepository
import com.raul.licenta.repository.UserRepository
import com.raul.licenta.repository.UserRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import javax.transaction.Transactional

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,

    @Autowired
    private val userRoleRepository: UserRoleRepository,

    @Autowired
    private val imageRepository: ImageRepository,

    @Autowired
    private val activationRepository: ActivationRepository,

    @Autowired
    private val emailService: EmailService
) {
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun register(user: User) {
        if (userRepository.findByUsername(user.username).isPresent) {
            throw ServerBaseException(ExceptionMessage.UserAlreadyRegistered)
        }
        userRepository.save(user)
        val builder = StringBuilder()
        builder.append("Your account needs activation! <br>")
        builder.append("This is done in 2 steps: <br>")
        builder.append("1. Press <a href='http://localhost:8080/unauthenticated/user/")
        builder.append(user.id)
        builder.append("/activate/")
        builder.append(user.activation.uuid)
        builder.append("'>here</a> to activate your email! <br>")
        builder.append("2. Send an email to raulapp.documents@gmail.com with the documents")
        builder.append(" that proves your rank as a doctor!")
        emailService.send(user.username, Email("Activation required", builder.toString()))
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

    fun activate(id: Long, uuid: String) {
        val user = userRepository.findById(id)
        if (user.isEmpty) {
            return
        }
        if (user.get().activation.uuid != uuid || user.get().activation.activated) {
            return
        }
        user.get().activation.activated = true
        activationRepository.save(user.get().activation)
        var builder = StringBuilder()
        builder.append("The first step in you account activation completed successfully! <br>")
        builder.append("All you need to do now is to send an email to raulapp.documents@gmail.com with the documents")
        builder.append(" that proves your rank as a doctor!")
        emailService.send(user.get().username, Email("Activation notification", builder.toString()))
        builder = StringBuilder()
        builder.append("New user created: <br>")
        builder.append("Name: ")
        builder.append(user.get().name)
        builder.append("<br>Email: ")
        builder.append(user.get().username)
        builder.append("<br> You can use the administration panel in the app to set the role of this user!")
        emailService.send("raulapp.documents@gmail.com", Email("Activation notification", builder.toString()))
    }
}