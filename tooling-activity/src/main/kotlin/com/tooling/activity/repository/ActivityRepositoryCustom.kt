package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.Activity
import com.tooling.activity.model.CodeDuration
import com.tooling.activity.model.DateInterval
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

interface ActivityRepositoryCustom {

  fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<Activity>

  fun findDuration(tenantId: Mono<String>, interval: DateInterval): Flux<CodeDuration>

  fun update(activity: Activity): Mono<UpdateResult>

  fun deleteByIdAndTenantId(id: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult>

}