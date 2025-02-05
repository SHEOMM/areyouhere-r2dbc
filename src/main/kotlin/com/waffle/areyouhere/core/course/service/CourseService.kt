package com.waffle.areyouhere.core.course.service

import com.waffle.areyouhere.core.course.model.Course
import com.waffle.areyouhere.core.course.repository.CourseRepository
import com.waffle.areyouhere.core.course.service.dto.CourseDto
import com.waffle.areyouhere.core.course.service.dto.CourseSaveDto
import com.waffle.areyouhere.crossConcern.error.CourseNotFoundException
import org.springframework.stereotype.Service

@Service
class CourseService(
    private val courseRepository: CourseRepository,
) {
    suspend fun save(courseDto: CourseSaveDto): CourseDto {
        return courseRepository.save(Course(courseDto))
            .let { CourseDto(it) }
    }

    suspend fun getAll(managerId: Long): List<CourseDto> {
        return courseRepository.findByManagerId(managerId)
            .map { CourseDto(it) }
    }

    suspend fun get(courseId: Long): CourseDto {
        return courseRepository.findById(courseId)?.let {
            CourseDto(it)
        } ?: throw CourseNotFoundException
    }

    suspend fun update(courseDto: CourseDto): CourseDto {
        return courseRepository.save(Course(courseDto))
            .let { CourseDto(it) }
    }

    suspend fun delete(courseDto: CourseDto) {
        courseRepository.delete(Course(courseDto))
    }
}
