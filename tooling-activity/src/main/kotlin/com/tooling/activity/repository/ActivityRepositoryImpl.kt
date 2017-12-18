package com.tooling.activity.repository

import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.UpdateResult
import com.tooling.activity.model.Activity
import com.tooling.activity.model.CodeDuration
import com.tooling.activity.model.DateInterval
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.*
import java.util.Calendar



class ActivityRepositoryImpl(private val reactiveMongoTemplate: ReactiveMongoTemplate): ActivityRepositoryCustom {

  override fun find(tenantId: Mono<String>, queryFields: MultiValueMap<String, String>): Flux<Activity> {
    val query = Query(Criteria.where("tenantId").`is`(tenantId.block()))

    for (queryField in queryFields.entries) {
      if ("start" == queryField.key) {
        if (queryField.value.size == 1) {
          val start = Instant.ofEpochSecond(queryField.value[0].toLong())
          query.addCriteria(Criteria.where("startDate").gte(start))
        }
      } else if ("end" == queryField.key) {
        if (queryField.value.size == 1) {
          val end = Instant.ofEpochSecond(queryField.value[0].toLong())
          query.addCriteria(Criteria.where("endDate").lte(end))
        }
      } else {
        query.addCriteria(Criteria.where(queryField.key).`in`(queryField.value))
      }
    }

    return reactiveMongoTemplate.find(query, Activity::class.java)
  }

  override fun findDuration(tenantId: Mono<String>, interval: DateInterval): Flux<CodeDuration> {
    val criteria = Criteria.where("tenantId").`is`(tenantId.block())

    if (interval.userIds != null && interval.userIds.size > 0) {
      criteria.and("userId").`in`(interval.userIds)
    }

    if (interval.projects != null && interval.projects.size > 0) {
      criteria.and("code").`in`(interval.projects)
    }

    if (interval.startDate != null) {
      criteria.and("startDate").gte(Date.from(interval.startDate))
    } else {
      val cal = Calendar.getInstance()
      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
      criteria.and("startDate").gte(Date.from(Instant.from(cal.toInstant())))
    }

    if (interval.endDate != null) {
      criteria.and("endDate").gte(Date.from(interval.endDate))
    }

    val grp: GroupOperation
    var projection = Aggregation.project().andExpression("duration").`as`("duration")

    if (interval.grpByCode) {
      if (interval.grpByType && interval.grpByUser) {
        grp = Aggregation.group("code", "userId", "typeCode").sum("duration").`as`("duration")
        projection = projection
          .andExpression("code").`as`("code")
          .andExpression("typeCode").`as`("typeCode")
          .andExpression("userId").`as`("userId")
      } else if (interval.grpByType && !interval.grpByUser) {
        grp = Aggregation.group("code", "typeCode").sum("duration").`as`("duration")
        projection = projection
          .andExpression("code").`as`("code")
          .andExpression("typeCode").`as`("typeCode")
      } else if (!interval.grpByType && interval.grpByUser) {
        grp = Aggregation.group("code", "userId").sum("duration").`as`("duration")
        projection = projection
          .andExpression("code").`as`("code")
          .andExpression("userId").`as`("userId")
      } else {
        grp = Aggregation.group("code").sum("duration").`as`("duration")
        projection = projection.andExpression("_id").`as`("code")
      }
    } else {
        grp = Aggregation.group("userId", "typeCode").sum("duration").`as`("duration")
        projection = projection
          .andExpression("typeCode").`as`("typeCode")
          .andExpression("userId").`as`("userId")
    }


    val aggregation = Aggregation.newAggregation(Aggregation.match(criteria), grp, projection)
    return reactiveMongoTemplate.aggregate(aggregation, "activity", CodeDuration::class.java)
  }

  override fun update(activity: Activity): Mono<UpdateResult> {
    val query = Query(Criteria
      .where("id").`is`(activity.id)
      .and("tenantId").`is`(activity.tenantId))
    val update = Update()
    update.set("typeCode", activity.typeCode)
    update.set("duration", activity.duration)
    update.set("startDate", activity.startDate)
    update.set("endDate", activity.endDate)
    update.set("code", activity.code)
    update.set("comment", activity.comment)
    return reactiveMongoTemplate.updateFirst(query, update, Activity::class.java)
  }

  override fun deleteByIdAndTenantId(id: Mono<String>, tenantId: Mono<String>): Mono<DeleteResult> {
    val query = Query(Criteria
      .where("id").`is`(id.block())
      .and("tenantId").`is`(tenantId.block()))
    return reactiveMongoTemplate.remove(query, Activity::class.java)
  }
}