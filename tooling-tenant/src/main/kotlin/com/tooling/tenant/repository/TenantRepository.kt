package com.tooling.tenant.repository

import com.tooling.tenant.model.Tenant
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface TenantRepository : ReactiveMongoRepository<Tenant, String> {
  fun findByName(name: String): Flux<Tenant>
}