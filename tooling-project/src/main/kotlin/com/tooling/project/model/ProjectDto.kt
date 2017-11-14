package com.tooling.project.model

data class ProjectDto constructor(var code: String,
                                  var name: String,
                                  var status: ProjectStatus) {
  constructor(project: Project): this(project.code, project.name, project.status)
}