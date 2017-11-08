package com.tooling.vacation.router

import com.tooling.vacation.vacation.VacationHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
open class VacationRouter(private val vacationHandler: VacationHandler) {

  @Bean
  open fun vacationApis() = router {
    (accept(MediaType.APPLICATION_JSON) and "/").nest {
      GET("/", { _ -> vacationHandler.find() })
      POST("/", vacationHandler::create)
      PUT("/{id}", vacationHandler::update)
    }
  }
}