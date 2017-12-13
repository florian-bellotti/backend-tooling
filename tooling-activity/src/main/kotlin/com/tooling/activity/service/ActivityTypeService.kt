package com.tooling.activity.service

import com.tooling.activity.model.Activity
import com.tooling.activity.model.ActivityDto
import com.tooling.activity.model.ActivityType
import com.tooling.activity.model.ActivityTypeDto
import com.tooling.activity.repository.ActivityRepository
import com.tooling.activity.repository.ActivityTypeRepository
import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.core.service.HeaderReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class ActivityTypeService(private val activityTypeRepository: ActivityTypeRepository) {

  companion object {
    val ADMIN_GROUP = Flux.just("ADMIN", "ACTIVITY_ADMIN")
  }

  fun insert(typeDto: ActivityTypeDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapActivityTypeDtoToActivityType(typeDto, request)
          .flatMap(activityTypeRepository::insert)
      )

  fun update(typeDto: ActivityTypeDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapActivityTypeDtoToActivityType(typeDto, request)
          .flatMap(activityTypeRepository::update)
      )

  fun delete(tenantId: Mono<String>, id: Mono<String>, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        activityTypeRepository.deleteByIdAndTenantId(id, tenantId)
      )

  private fun mapActivityTypeDtoToActivityType(activityTypeDto: ActivityTypeDto, request: ServerRequest) =
    HeaderReader.getTenantId(request)
      .map { tenantId -> ActivityType(activityTypeDto.id, activityTypeDto.code, activityTypeDto.name, tenantId) }
}