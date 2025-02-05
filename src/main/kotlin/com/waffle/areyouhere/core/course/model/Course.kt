package com.waffle.areyouhere.core.course.model

import com.waffle.areyouhere.core.course.service.dto.CourseDto
import com.waffle.areyouhere.core.course.service.dto.CourseSaveDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("course")
data class Course(
    @Id
    var id: Long? = null,
    var name: String,
    var description: String? = null,
    var allowOnlyRegistered: Boolean,
    var managerId: Long,
    @CreatedDate
    var createdAt: Instant? = null,
    @LastModifiedDate
    var updatedAt: Instant? = null,
) {
    constructor(courseDto: CourseSaveDto) : this(
        name = courseDto.name,
        description = courseDto.description,
        allowOnlyRegistered = courseDto.allowOnlyRegistered,
        managerId = courseDto.managerId,
    )

    constructor(courseDto: CourseDto) : this(
        id = courseDto.id,
        name = courseDto.name,
        description = courseDto.description,
        allowOnlyRegistered = courseDto.allowOnlyRegistered,
        managerId = courseDto.managerId,
    )
}
