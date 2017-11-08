package com.tooling.project.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Project(@Id
                   var code: String,
                   var name: String,
                   var status: ProjectStatus)