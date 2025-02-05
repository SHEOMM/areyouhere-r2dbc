package com.waffle.areyouhere.core.course.service

import com.waffle.areyouhere.api.dto.attendee.AttendeeNameAndNote
import com.waffle.areyouhere.core.attendee.service.AttendeeService
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeDto
import com.waffle.areyouhere.core.course.service.dto.CourseAndAttendeesCountDto
import com.waffle.areyouhere.core.course.service.dto.CourseDto
import com.waffle.areyouhere.core.course.service.dto.CourseSaveDto
import com.waffle.areyouhere.crossConcern.error.AttendeeNotUniqueException
import com.waffle.areyouhere.crossConcern.error.UnAuthorizeException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseFlowService(
    private val courseService: CourseService,
    private val attendeeService: AttendeeService,
) {

    @Transactional
    suspend fun create(managerId: Long, name: String, description: String?, attendeeNameAndNotes: List<AttendeeNameAndNote>) {
        if (attendeeService.isAttendeeUnique(attendeeNameAndNotes.map { it.name + it.note }).not()) {
            throw AttendeeNotUniqueException
        }

        val course = CourseSaveDto(
            name = name,
            description = description,
            allowOnlyRegistered = true,
            managerId = managerId,
        )
        val savedCourse = courseService.save(course)

        val attendees = if (attendeeNameAndNotes.isNotEmpty()) {
            attendeeNameAndNotes.map {
                AttendeeDto(
                    name = it.name,
                    note = it.note,
                    courseId = savedCourse.id,
                )
            }
        } else null

        attendees?.let { attendeeService.insertBatch(it) }
    }

    @Transactional(readOnly = true)
    suspend fun get(courseId: Long, managerId: Long): CourseDto {
        val course = courseService.get(courseId)
        throwIfCourseAuthorizationFail(managerId, course)
        return courseService.get(courseId)
    }

    @Transactional(readOnly = true)
    suspend fun getAllCourseAndAttendeesCountDto(managerId: Long): List<CourseAndAttendeesCountDto> {
        val courses = courseService.getAll(managerId)
        return courses.map {
            CourseAndAttendeesCountDto(
                id = it.id,
                name = it.name,
                description = it.description,
                allowOnlyRegistered = it.allowOnlyRegistered,
                // FIXME: 쿼리 하나로?
                attendees = attendeeService.countInCourse(it.id),
            )
        }
    }

    @Transactional
    suspend fun update(managerId: Long, courseId: Long, name: String, description: String?, allowOnlyRegistered: Boolean) {
        val courseDto = courseService.get(courseId)
        courseDto.update(name, description, allowOnlyRegistered)
        throwIfCourseAuthorizationFail(managerId, courseDto)
        courseService.update(courseDto)
    }

    @Transactional
    suspend fun delete(managerId: Long, courseId: Long) {
        val courseDto = courseService.get(courseId)
        throwIfCourseAuthorizationFail(managerId, courseDto)
        courseService.delete(courseDto)
    }

    private fun throwIfCourseAuthorizationFail(managerId: Long, course: CourseDto) {
        if (course.managerId != managerId) throw UnAuthorizeException
    }
}
