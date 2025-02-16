package com.waffle.areyouhere.core.section.model

import com.waffle.areyouhere.core.section.service.dto.SectionSaveDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("section")
data class Section(
    @Id
    var id: Long? = null,
    var name: String,
    var courseId: Long,
    @CreatedDate
    var createdAt: Instant? = null,
    @LastModifiedDate
    var updatedAt: Instant? = null,
) {
    constructor(sectionDto: SectionSaveDto) : this(
        name = sectionDto.name,
        courseId = sectionDto.courseId,
    )
}
