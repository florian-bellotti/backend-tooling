package com.tooling.project.model

data class ProjectRequest(var code: String,
                          var name: String,
                          var status: ProjectStatus)