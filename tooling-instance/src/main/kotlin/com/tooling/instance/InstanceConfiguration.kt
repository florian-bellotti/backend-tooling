package com.tooling.instance

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@ComponentScan
@EnableReactiveMongoRepositories
open class InstanceConfiguration