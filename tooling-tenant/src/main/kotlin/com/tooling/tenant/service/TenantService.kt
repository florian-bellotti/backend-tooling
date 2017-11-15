package com.tooling.tenant.service

import com.tooling.tenant.model.Tenant
import com.tooling.tenant.repository.TenantRepository
import org.springframework.stereotype.Component

@Component
open class TenantService(private val tenantRepository: TenantRepository) {

  open fun findAll() = tenantRepository.findAll()

  open fun findByName(name: String) = tenantRepository.findByName(name)

  open fun insertDefault() = tenantRepository.insert(Tenant("123456", "Tooling"))

}