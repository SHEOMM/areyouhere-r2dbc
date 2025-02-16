package com.waffle.areyouhere.core.section.service.dto

import com.waffle.areyouhere.core.attendance.model.AttendanceType
import com.waffle.areyouhere.core.attendance.service.dto.AttendanceDto
import com.waffle.areyouhere.core.section.repository.dto.SectionWithAttendance
import java.time.Instant
import java.time.ZoneOffset

data class SectionWithAttendanceDto(
    val id: Long,
    val date: Instant,
    val name: String,
    val attendee: Int,
    val absentee: Int,
    val late: Int,
) {
    constructor(sectionWithAttendance: SectionWithAttendance) : this(
        id = sectionWithAttendance.getid()!!,
        date = sectionWithAttendance.getdate()?.toInstant(ZoneOffset.of("Asia/Seoul"))!!,
        name = sectionWithAttendance.getname()!!,
        attendee = sectionWithAttendance.getattendee(),
        absentee = sectionWithAttendance.getabsentee(),
        late = sectionWithAttendance.getlate(),
    )

    constructor(section: SectionDto, attendance: List<AttendanceDto>) : this(
        id = section.id,
        date = section.createdAt,
        name = section.name,
        attendee = attendance.count { it.status == AttendanceType.ATTENDED },
        absentee = attendance.count { it.status == AttendanceType.ABSENT },
        late = attendance.count { it.status == AttendanceType.LATE },
    )
}
