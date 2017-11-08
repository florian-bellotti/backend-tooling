package com.tooling.vacation

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
open class VacationApplication

fun main(args: Array<String>) {
  SpringApplication.run(VacationApplication::class.java, *args)
}