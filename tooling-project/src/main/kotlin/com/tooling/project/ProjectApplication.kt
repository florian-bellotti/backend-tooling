package com.tooling.project

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
open class ProjectApplication

fun main(args: Array<String>) {
  SpringApplication.run(ProjectApplication::class.java, *args)
}