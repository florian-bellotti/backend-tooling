package com.tooling.user

import com.tooling.tenant.TenantConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@ComponentScan
@Configuration
@EnableMongoRepositories
@Import(TenantConfiguration::class)
open class UserConfiguration