package com.tooling.instance.repository

import com.tooling.instance.model.Instance
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface InstanceRepository: ReactiveMongoRepository<Instance, String> {
  fun findByName(name: String): Flux<Instance>
}