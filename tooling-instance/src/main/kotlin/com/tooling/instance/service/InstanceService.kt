package com.tooling.instance.service

import com.tooling.instance.repository.InstanceRepository
import org.springframework.stereotype.Component

@Component
open class InstanceService(private val instanceRepository: InstanceRepository) {

  open fun findAll() = instanceRepository.findAll()

  open fun findByName(name: String) = instanceRepository.findByName(name)

}