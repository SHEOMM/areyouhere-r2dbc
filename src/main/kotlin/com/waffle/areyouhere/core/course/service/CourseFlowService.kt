package com.waffle.areyouhere.core.course.service

import com.waffle.areyouhere.core.attendee.model.Attendee
import com.waffle.areyouhere.core.attendee.service.AttendeeService
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeDto
import com.waffle.areyouhere.core.course.model.Course
import com.waffle.areyouhere.core.course.service.dto.CourseAndAttendeesCountDto
import com.waffle.areyouhere.crossConcern.error.AttendeeNotUniqueException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseFlowService(
    private val courseService: CourseService,
    private val attendeeService: AttendeeService,
) {

    @Transactional
    suspend fun create(managerId: Long, name: String, description: String?, attendeeDtos: List<AttendeeDto>) {
        if (attendeeService.isAttendeeUnique(attendeeDtos.map { it.name + it.note }).not()) {
            throw AttendeeNotUniqueException
        }

        val course = Course(
            name = name,
            description = description,
            allowOnlyRegistered = true,
            managerId = managerId,
        )
        val savedCourse = courseService.save(course)

        val attendees = attendeeDtos.map {
            Attendee(
                name = it.name,
                note = it.note,
                courseId = savedCourse.id!!,
            )
        }
        attendeeService.insertBatch(attendees)
    }

    @Transactional(readOnly = true)
    suspend fun getAllCourseAndAttendeesCountDto(managerId: Long): List<CourseAndAttendeesCountDto> {
        val courses = courseService.getAll(managerId)
        return courses.map {
            CourseAndAttendeesCountDto(
                id = it.id!!,
                name = it.name,
                description = it.description,
                allowOnlyRegistered = it.allowOnlyRegistered,
                // FIXME: 쿼리 하나로?
                attendeesCount = attendeeService.countInCourse(it.id!!),
            )
        }
    }
}
