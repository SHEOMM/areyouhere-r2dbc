package com.waffle.areyouhere.core.course.service.dto

import com.waffle.areyouhere.core.course.model.Course

data class CourseDto(
    val id: Long,
    val name: String,
    val description: String?,
    val allowOnlyRegistered: Boolean,
    val managerId: Long,
) {
    constructor(course: Course) : this(
        id = course.id!!,
        name = course.name,
        description = course.description,
        allowOnlyRegistered = course.allowOnlyRegistered,
        managerId = course.managerId,
    )

    fun update(name: String, description: String?, allowOnlyRegistered: Boolean) =
        copy(
            name = name,
            description = description,
            allowOnlyRegistered = allowOnlyRegistered,
        )
}

data class CourseSaveDto(
    val name: String,
    val description: String?,
    val allowOnlyRegistered: Boolean,
    val managerId: Long,
)
