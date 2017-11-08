package com.tooling.project.repository

import com.tooling.project.model.Project
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface ProjectRepository: ReactiveMongoRepository<Project, String>