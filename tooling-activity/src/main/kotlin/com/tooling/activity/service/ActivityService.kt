package com.tooling.activity.service

import com.tooling.activity.model.Activity
import com.tooling.activity.model.ActivityDto
import com.tooling.activity.repository.ActivityRepository
import com.tooling.core.service.HeaderReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ActivityService(private val activityRepository: ActivityRepository) {

  companion object {
    val ADMIN_GROUP = Flux.just("ADMIN", "ACTIVITY_ADMIN")
  }

  fun insert(activityDto: ActivityDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapActivityDtoToActivity(activityDto, request)
          .flatMap(activityRepository::insert)
      )

  fun update(activityDto: ActivityDto, request: ServerRequest, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(
        mapActivityDtoToActivity(activityDto, request)
          .flatMap(activityRepository::update)
      )

  fun delete(tenantId: Mono<String>, id: Mono<String>, groups: Flux<String>) =
    HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
      .then(activityRepository.deleteByIdAndTenantId(id, tenantId))

  private fun mapActivityDtoToActivity(activityDto: ActivityDto, request: ServerRequest) =
    HeaderReader.getTenantId(request)
      .map { tenantId ->
        Activity(activityDto.id, activityDto.userId,
          activityDto.code, activityDto.startDate,
          activityDto.endDate, activityDto.comment, tenantId)
      }
}