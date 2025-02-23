package com.waffle.areyouhere.core.attendee.service.dto

data class AttendeeSaveDto(
    val name: String,
    val note: String?,
    val courseId: Long,
) {
    constructor(attendeeDto: AttendeeDto) : this(
        name = attendeeDto.name,
        note = attendeeDto.note,
        courseId = attendeeDto.courseId,
    )
}
