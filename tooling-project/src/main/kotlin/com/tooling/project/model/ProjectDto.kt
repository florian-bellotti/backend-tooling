package com.tooling.project.model

data class ProjectDto constructor(var id: String? = null,
                                  var code: String,
                                  var name: String,
                                  var description: String? = null,
                                  var color: String? = null,
                                  var properties: Map<String, ProjectProperty>? = null,
                                  var status: ProjectStatus) {
  constructor(project: Project): this(project.id, project.code, project.name,
    project.description, project.color, project.properties, project.status)
}