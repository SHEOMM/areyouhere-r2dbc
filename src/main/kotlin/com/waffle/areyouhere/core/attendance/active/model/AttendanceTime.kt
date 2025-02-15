package com.waffle.areyouhere.core.attendance.active.model

import com.waffle.areyouhere.core.attendance.model.AttendanceType
import java.time.Instant

data class AttendanceTime(
    val attendedAt: Instant,
    val status: AttendanceType,
)
