package com.raul.licenta.controller

import com.raul.licenta.dto.RegisterDto
import com.raul.licenta.dto.UserDto
import com.raul.licenta.mapper.UserMapper
import com.raul.licenta.model.Role
import com.raul.licenta.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
        @Autowired
        private val userService: UserService,

        @Autowired
        private val userMapper: UserMapper
) {
//    @GetMapping("/authenticated/")
//    fun auto() : ResponseEntity<List<User>> {
//        return ResponseEntity(userService.getAllUsers(), HttpStatus.OK)
//    }
    @PostMapping("/unauthenticated/user")
    fun register(@RequestBody dto: RegisterDto) {
        userService.register(userMapper.getUser(dto))
    }

    @GetMapping("/admin/user/all")
    fun getAll() : List<UserDto> = userMapper.getUsers(userService.getAllUsers())

    @PostMapping("/admin/user/{id}")
    fun changeRoles(@PathVariable id: Long, @RequestBody roles: List<Role>) {
        userService.changeRoles(id, roles)
    }

    @GetMapping("/admin/user/{id}/delete")
    fun delete(@PathVariable id: Long) {
        userService.delete(id)
    }

}
