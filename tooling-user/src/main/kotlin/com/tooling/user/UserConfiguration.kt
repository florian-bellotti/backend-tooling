package com.tooling.user

import com.tooling.instance.InstanceConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

//@SpringBootApplication
@ComponentScan
@Configuration
@EnableMongoRepositories
@Import(InstanceConfiguration::class)
open class UserConfiguration

/*
fun main(args: Array<String>) {
  SpringApplication.run(UserConfiguration::class.java, *args)
}*/