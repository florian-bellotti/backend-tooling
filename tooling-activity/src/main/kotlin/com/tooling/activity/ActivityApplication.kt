package com.tooling.activity

import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
open class ActivityApplication

fun main(args: Array<String>) {
  SpringApplication.run(ActivityApplication::class.java, *args)
}