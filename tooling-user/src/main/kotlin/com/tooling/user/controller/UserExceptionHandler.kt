package com.tooling.user.controller

import com.tooling.instance.exception.InvalidInstanceIdException
import com.tooling.user.exception.DisabledUserException
import com.tooling.user.exception.InvalidCredentialsException
import com.tooling.user.model.ErrorMessage
import io.jsonwebtoken.JwtException
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*

@ResponseBody
@ControllerAdvice
open class UserExceptionHandler {
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidInstanceIdException::class)
  fun handleInvalidInstanceIdException() = ErrorMessage("BAD_REQUEST", "Instance id is invalid")

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(JwtException::class)
  fun handleJwtException() = ErrorMessage("UNAUTHORIZED", "Token is invalid")

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException() = ErrorMessage("FORBIDDEN", "Forbidden")

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(InvalidCredentialsException::class)
  fun handleInvalidCredentialsException() = ErrorMessage("INVALID_CREDENTIALS", "Invalid email/password")

  @ResponseStatus(FORBIDDEN)
  @ExceptionHandler(DisabledUserException::class)
  fun handleDisableUserException() = ErrorMessage("DISABLED_USER", "Disabled user")
}