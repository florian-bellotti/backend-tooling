package com.tooling.activity.repository

import com.tooling.activity.model.Activity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ActivityRepository: ReactiveMongoRepository<Activity, String>, ActivityRepositoryCustom