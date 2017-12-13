package com.tooling.activity.repository

import com.tooling.activity.model.ActivityType
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ActivityTypeRepository : ReactiveMongoRepository<ActivityType, String>, ActivityTypeRepositoryCustom