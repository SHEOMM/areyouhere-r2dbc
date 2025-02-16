package com.waffle.areyouhere.core.attendance.service.dto

import com.waffle.areyouhere.core.attendance.model.Attendance
import com.waffle.areyouhere.core.attendance.model.AttendanceType
import java.time.Instant

data class AttendanceDto(
    val id: Long,
    val attendeeId: Long,
    val sectionId: Long,
    val status: AttendanceType,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(attendance: Attendance) : this(
        id = attendance.id!!,
        attendeeId = attendance.attendeeId,
        sectionId = attendance.sectionId,
        status = attendance.status,
        createdAt = attendance.createdAt!!,
        updatedAt = attendance.updatedAt!!,
    )
}
