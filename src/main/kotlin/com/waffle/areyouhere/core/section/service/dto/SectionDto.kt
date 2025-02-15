package com.waffle.areyouhere.core.section.service.dto

import com.waffle.areyouhere.core.section.model.Section
import java.time.Instant

data class SectionDto(
    val id: Long,
    val name: String,
    val courseId: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(section: Section) : this(
        id = section.id!!,
        name = section.name,
        courseId = section.courseId,
        createdAt = section.createdAt!!,
        updatedAt = section.updatedAt!!,
    )
}
