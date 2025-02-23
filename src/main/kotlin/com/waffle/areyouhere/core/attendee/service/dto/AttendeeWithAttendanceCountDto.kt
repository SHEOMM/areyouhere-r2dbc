package com.waffle.areyouhere.core.attendee.service.dto

data class AttendeeWithAttendanceCountDto(
    val attendee: AttendeeSaveDto,
    val attendance: Int,
    val absence: Int,
    val late: Int,
)
