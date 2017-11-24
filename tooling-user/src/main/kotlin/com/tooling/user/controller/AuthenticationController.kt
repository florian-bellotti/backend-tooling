package com.tooling.user.controller

import com.tooling.user.model.TmpUser
import com.tooling.user.model.User
import com.tooling.user.model.UserDto
import com.tooling.user.model.UserLogin
import com.tooling.user.service.AuthenticationService
import com.tooling.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class AuthenticationController(private val authenticationService: AuthenticationService,
                               private val userService: UserService) {

  @GetMapping
  fun find(@RequestParam allRequestParams: Map<String,String>, request: HttpServletRequest) =
    userService.find(request.getHeader("tenantId"), allRequestParams)

  @PostMapping
  fun create(@RequestBody user: TmpUser, request: HttpServletRequest) =
    userService.create(user, request.getHeader("tenantId"), request.getHeader("grp"))

  @PutMapping
  fun update(@RequestBody user: UserDto, request: HttpServletRequest) =
    userService.update(user, request.getHeader("tenantId"), request.getHeader("grp"))

  @DeleteMapping("/{id}")
  fun delete(@PathVariable id: String, request: HttpServletRequest) =
    userService.delete(id, request.getHeader("tenantId"), request.getHeader("grp"))

  @PostMapping("/authenticate")
  fun authenticate(@RequestBody userLogin: UserLogin) = authenticationService.authenticate(userLogin)
}
