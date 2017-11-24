package com.tooling.project.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.project.model.Project
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectRepositoryCustom {
  fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<Project>
  fun update(project: Project): Mono<UpdateResult>
  fun deleteByCodeAndTenantId(code: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult>
}