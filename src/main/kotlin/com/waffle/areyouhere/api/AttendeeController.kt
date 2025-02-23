package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.attendee.service.AttendeeFlowService
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeWithAttendanceCountDto
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/attendee")
class AttendeeController(
    private val sessionManager: SessionManager,
    private val attendeeFlowService: AttendeeFlowService,
) {
    @GetMapping
    suspend fun getAllInCourseWithAttendances(
        @RequestParam("courseId") courseId: Long,
        session: WebSession,
    ): ResponseEntity<List<AttendeeWithAttendanceCountDto>> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val result = attendeeFlowService.getAllInCourseWithAttendance(managerId, courseId)
        return ResponseEntity.ok(result)
    }
}
