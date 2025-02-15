package com.waffle.areyouhere.core.attendance.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("attendance")
data class Attendance(
    @Id
    var id: Long? = null,
    var attendeeId: Long,
    var sectionId: Long,
    var status: AttendanceType,
    @CreatedDate
    var createdAt: Instant? = null,
    @LastModifiedDate
    var updatedAt: Instant? = null,
)
