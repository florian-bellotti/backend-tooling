package com.tooling.project.repository

import com.tooling.project.model.Project
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ProjectRepository: ReactiveMongoRepository<Project, String>, ProjectRepositoryCustom