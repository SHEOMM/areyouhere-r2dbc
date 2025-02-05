package com.waffle.areyouhere.api

import com.waffle.areyouhere.api.dto.attendee.AttendeeNameAndNote
import com.waffle.areyouhere.core.course.service.CourseFlowService
import com.waffle.areyouhere.core.course.service.dto.CourseAndAttendeesCountDto
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    suspend fun create(@RequestBody courseCreateRequest: CourseCreateRequest, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        courseFlowService.create(
            managerId = managerId,
            name = courseCreateRequest.name,
            description = courseCreateRequest.description,
            attendeeNameAndNotes = courseCreateRequest.attendees,
        )
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/{courseId}")
    suspend fun get(@PathVariable courseId: Long, session: WebSession): ResponseEntity<CourseGetResponse> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val result = courseFlowService.get(courseId, managerId)
        return ResponseEntity.ok(
            CourseGetResponse(
                result.id,
                result.name,
                result.description,
                result.allowOnlyRegistered,
            ),
        )
    }

    @GetMapping
    suspend fun getAll(session: WebSession): ResponseEntity<CoursesResponse> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val response = CoursesResponse(courseFlowService.getAllCourseAndAttendeesCountDto(managerId))
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{courseId}")
    suspend fun update(
        @PathVariable courseId: Long,
        @RequestBody courseUpdateRequest: CourseUpdateRequest,
        session: WebSession,
    ): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        courseFlowService.update(
            managerId,
            courseId,
            courseUpdateRequest.name,
            courseUpdateRequest.description,
            courseUpdateRequest.allowOnlyRegistered,
        )

        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @DeleteMapping("/{courseId}")
    suspend fun delete(@PathVariable courseId: Long, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        courseFlowService.delete(managerId, courseId)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    data class CoursesResponse(
        val courses: List<CourseAndAttendeesCountDto>,
    )

    data class CourseCreateRequest(
        val name: String,
        val description: String? = null,
        val attendees: List<AttendeeNameAndNote>,
        val onlyListNameAllowed: Boolean? = true,
    )

    data class CourseGetResponse(
        val id: Long,
        val name: String,
        val description: String?,
        val allowOnlyRegistered: Boolean,
    )

    data class CourseUpdateRequest(
        val name: String,
        val description: String?,
        val allowOnlyRegistered: Boolean,
    )
}
