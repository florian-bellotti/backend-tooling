package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.ActivityType
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ActivityTypeRepositoryCustom {
  fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<ActivityType>
  fun update(type: ActivityType): Mono<UpdateResult>
  fun deleteByIdAndTenantId(id: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult>
}