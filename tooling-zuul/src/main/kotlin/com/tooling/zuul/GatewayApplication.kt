package com.tooling.zuul

import com.fbellotti.tooling.zuul.filter.SimpleFilter
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

@EnableZuulProxy
@SpringBootApplication
open class GatewayApplication {
  @Bean
  open fun simpleFilter() = SimpleFilter()
}

fun main(args: Array<String>) {
  SpringApplication.run(GatewayApplication::class.java, *args)
}