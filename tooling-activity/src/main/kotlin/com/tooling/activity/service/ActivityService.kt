package com.tooling.activity.service

import com.tooling.activity.model.Activity
import com.tooling.activity.model.ActivityDto
import com.tooling.activity.repository.ActivityRepository
import com.tooling.core.exception.InvalidUserGroupException
import com.tooling.core.service.HeaderReader
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class ActivityService(private val activityRepository: ActivityRepository) {

  companion object {
    val ADMIN_GROUP = Flux.just("ADMIN", "ACTIVITY_ADMIN")
  }

  /*fun findDurationInDay(request: ServerRequest) =
     activityRepository.findDuration(HeaderReader.getTenantId(request))
*/

  fun insert(activityDto: ActivityDto, request: ServerRequest, userIds: Mono<List<String>>, groups: Flux<String>) =
    userIds
      .flatMap { ids ->
        if (ids.size != 1)
          throw InvalidUserGroupException("User id is null in header");

        if (!activityDto.userId.equals(ids[0])) {
          HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
            .then(
              mapActivityDtoToActivity(activityDto, request)
                .flatMap(activityRepository::insert)
            )
        } else {
          mapActivityDtoToActivity(activityDto, request)
            .flatMap(activityRepository::insert)
        }
      }

  fun update(activityDto: ActivityDto, request: ServerRequest, userIds: Mono<List<String>>, groups: Flux<String>) =
    userIds
      .flatMap { ids ->
        if (ids.size != 1)
          throw InvalidUserGroupException("User id is null in header");

        if (!activityDto.userId.equals(ids[0])) {
          HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
            .then(
              mapActivityDtoToActivity(activityDto, request)
                .flatMap(activityRepository::update)
            )
        } else {
          mapActivityDtoToActivity(activityDto, request)
            .flatMap(activityRepository::update)
        }
      }

  fun delete(tenantId: Mono<String>, id: Mono<String>, userIds: Mono<List<String>>, groups: Flux<String>) =
    userIds
      .flatMap { ids ->
        if (ids.size != 1)
          throw InvalidUserGroupException("User id is null in header");

        activityRepository
          .findById(id)
          .flatMap { activity ->
            if (ids[0].equals(activity.userId)) {
              HeaderReader.oneRuleMatch(groups, ADMIN_GROUP)
              .then(activityRepository.deleteByIdAndTenantId(id, tenantId))
            } else {
              activityRepository.deleteByIdAndTenantId(id, tenantId)
            }
          }
      }

  private fun mapActivityDtoToActivity(activityDto: ActivityDto, request: ServerRequest) =
    HeaderReader.getTenantId(request)
      .map { tenantId ->
        Activity(activityDto.id, activityDto.userId,
          activityDto.code, activityDto.typeCode, Duration.between(activityDto.startDate, activityDto.endDate).seconds,
          activityDto.startDate, activityDto.endDate, activityDto.comment, tenantId)
      }
}