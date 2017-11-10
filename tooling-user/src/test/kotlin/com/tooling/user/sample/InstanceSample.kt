package com.tooling.user.sample

import com.tooling.instance.model.Instance
import reactor.core.publisher.Flux

object InstanceSample {
  val INSTANCE = Instance("123", "test1")
  val INSTANCES_MONO = Flux.fromArray(arrayOf(Instance("123", "test1"), Instance("234", "test2")))
}