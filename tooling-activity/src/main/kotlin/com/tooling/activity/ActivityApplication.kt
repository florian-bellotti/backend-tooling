package com.tooling.activity

import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration

@SpringBootConfiguration
open class ActivityApplication

fun main(args: Array<String>) {
  SpringApplication.run(ActivityApplication::class.java, *args)
}