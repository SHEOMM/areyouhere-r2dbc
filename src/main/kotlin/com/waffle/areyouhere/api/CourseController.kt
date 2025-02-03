package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.attendee.service.dto.AttendeeDto
import com.waffle.areyouhere.core.course.service.CourseFlowService
import com.waffle.areyouhere.core.course.service.dto.CourseAndAttendeesCountDto
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/course")
class CourseController(
    private val courseFlowService: CourseFlowService,
    private val sessionManager: SessionManager,
) {
    @PostMapping
    suspend fun create(@RequestBody courseCreateRequestDto: CourseCreateRequestDto, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        courseFlowService.create(
            managerId = managerId,
            name = courseCreateRequestDto.name,
            description = courseCreateRequestDto.description,
            attendeeDtos = courseCreateRequestDto.attendees,
        )
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @GetMapping
    suspend fun getAll(session: WebSession): ResponseEntity<CoursesResponseDto> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val response = CoursesResponseDto(courseFlowService.getAllCourseAndAttendeesCountDto(managerId))
        return ResponseEntity.ok(response)
    }

    data class CoursesResponseDto(
        val courses: List<CourseAndAttendeesCountDto>,
    )

    data class CourseCreateRequestDto(
        val name: String,
        val description: String? = null,
        val attendees: List<AttendeeDto>,
        val onlyListNameAllowed: Boolean? = true,
    )
}
