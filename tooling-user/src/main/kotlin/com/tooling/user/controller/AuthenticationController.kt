package com.tooling.user.controller

import com.tooling.user.model.UserLogin
import com.tooling.user.service.AuthenticationService
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class AuthenticationController(private val authenticationService: AuthenticationService) {

  @PostMapping("/authenticate")
  fun authenticate(@RequestBody userLogin: UserLogin) =
    authenticationService.authenticate(userLogin)
}
