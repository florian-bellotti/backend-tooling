package com.tooling.project.router

import com.tooling.project.handler.ProjectHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
open class ProjectRouter(private val projectHandler: ProjectHandler) {

  @Bean
  open fun vacationApis() = router {
    (accept(MediaType.APPLICATION_JSON) and "/").nest {
      GET("/", projectHandler::find)
      POST("/", projectHandler::create)
      PUT("/", projectHandler::update)
      DELETE("/{id}", projectHandler::delete)
    }
  }
}