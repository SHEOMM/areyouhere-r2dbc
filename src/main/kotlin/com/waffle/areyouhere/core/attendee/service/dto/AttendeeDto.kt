package com.waffle.areyouhere.core.attendee.service.dto

import com.waffle.areyouhere.core.attendee.model.Attendee
import java.time.Instant

data class AttendeeDto(
    val id: Long,
    val name: String,
    val note: String?,
    val courseId: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(attendee: Attendee) : this(
        id = attendee.id!!,
        name = attendee.name,
        note = attendee.note,
        courseId = attendee.courseId,
        createdAt = attendee.createdAt!!,
        updatedAt = attendee.updatedAt!!,
    )
}
