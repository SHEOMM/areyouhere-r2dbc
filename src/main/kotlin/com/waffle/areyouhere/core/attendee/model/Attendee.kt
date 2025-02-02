package com.waffle.areyouhere.core.attendee.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "attendee")
data class Attendee(
    @Id
    var id: Long? = null,
    var name: String,
    var note: String? = null,
    var courseId: Long,
    @CreatedDate
    var createdAt: Instant? = null,
    @LastModifiedDate
    var updatedAt: Instant? = null,
)
