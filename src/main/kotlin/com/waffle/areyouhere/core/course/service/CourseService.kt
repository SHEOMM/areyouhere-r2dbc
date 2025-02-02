package com.waffle.areyouhere.core.course.service

import com.waffle.areyouhere.core.course.model.Course
import com.waffle.areyouhere.core.course.repository.CourseRepository
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    suspend fun getAll(managerId: Long): List<Course> {
        return courseRepository.findByManagerId(managerId)
    }
    suspend fun save(course: Course): Course {
        return courseRepository.save(course)
    }
}
