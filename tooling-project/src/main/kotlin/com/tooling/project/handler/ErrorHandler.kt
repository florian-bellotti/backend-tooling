package com.tooling.project.handler

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.project.exception.InvalidProjectException
import com.tooling.project.model.ErrorResponse
import com.tooling.tenant.exception.InvalidTenantIdException
import org.slf4j.LoggerFactory
import org.springframework.core.codec.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

@Component
class ErrorHandler: WebExceptionHandler {

  companion object {
    private val logger = LoggerFactory.getLogger(ErrorHandler::class.java)
  }

  override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> =
    handle(ex)
      .flatMap {
        it.writeTo(exchange, HandlerStrategiesResponseContext(HandlerStrategies.withDefaults()))
      }
      .flatMap {
        Mono.empty<Void>()
      }

  fun handle(throwable: Throwable): Mono<ServerResponse> {
    return when (throwable) {
      InvalidTenantIdException::class, InvalidProjectException::class, DecodingException::class -> {
        handleError(BAD_REQUEST, throwable)
      }
      InvalidUserGroupException::class -> {
        handleError(FORBIDDEN, throwable)
      }
      else -> {
        logger.error(INTERNAL_SERVER_ERROR.toString(), throwable)
        createResponse(INTERNAL_SERVER_ERROR, throwable.message)
      }
    }
  }

  fun handleError(httpStatus: HttpStatus, throwable: Throwable): Mono<ServerResponse> {
    logger.error(httpStatus.toString(), throwable)
    return createResponse(httpStatus, throwable.message)
  }

  fun createResponse(httpStatus: HttpStatus, message: String?): Mono<ServerResponse> =
    ServerResponse.status(httpStatus).syncBody(ErrorResponse(httpStatus, message))
}


private class HandlerStrategiesResponseContext(val strategies: HandlerStrategies) : ServerResponse.Context {

  override fun messageWriters(): List<HttpMessageWriter<*>> {
    return this.strategies.messageWriters()
  }

  override fun viewResolvers(): List<ViewResolver> {
    return this.strategies.viewResolvers()
  }
}