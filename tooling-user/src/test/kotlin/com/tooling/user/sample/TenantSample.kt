package com.tooling.user.sample

import com.tooling.tenant.model.Tenant
import reactor.core.publisher.Flux

object TenantSample {
  val TENANT = Tenant("123", "test1")
  val TENANTS_MONO = Flux.fromArray(arrayOf(Tenant("123", "test1"), Tenant("234", "test2")))
}