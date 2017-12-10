package com.tooling.activity.router

import com.tooling.activity.handler.ActivityHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
open class ActivityRouter(private val activityHandler: ActivityHandler) {

  @Bean
  open fun activityApi() = router {
    (accept(MediaType.APPLICATION_JSON) and "/").nest {
      GET("/", activityHandler::find)
      POST("/aggregateDurations", activityHandler::findDuration)
      POST("/", activityHandler::create)
      PUT("/", activityHandler::update)
      DELETE("/{id}", activityHandler::delete)
    }
  }

}