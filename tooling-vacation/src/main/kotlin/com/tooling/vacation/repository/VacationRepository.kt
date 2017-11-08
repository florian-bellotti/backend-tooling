package com.tooling.vacation.repository

import com.tooling.vacation.model.Vacation
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface VacationRepository: ReactiveMongoRepository<Vacation, String>