package com.waffle.areyouhere.core.course.service.dto

import com.waffle.areyouhere.core.course.model.Course

data class CourseAndAttendeesCountDto(
    val id: Long,
    val name: String,
    val description: String?,
    val allowOnlyRegistered: Boolean,
    val attendees: Long,
) {
    constructor(course: Course, attendeesCount: Long) : this(
        id = course.id!!,
        name = course.name,
        description = course.description,
        allowOnlyRegistered = course.allowOnlyRegistered,
        attendees = attendeesCount,
    )
}
