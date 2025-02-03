package com.waffle.areyouhere.core.course.repository

import com.waffle.areyouhere.core.course.model.Course
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CourseRepository : CoroutineCrudRepository<Course, Long> {
    suspend fun findByManagerId(managerId: Long): List<Course>
}
