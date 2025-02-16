package com.waffle.areyouhere.core.section.service.dto

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
}
