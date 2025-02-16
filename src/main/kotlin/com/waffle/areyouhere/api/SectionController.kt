package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.section.service.SectionFlowService
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/session")
class SectionController(
    private val sessionManager: SessionManager,
    private val sectionFlowService: SectionFlowService,
) {

    @PostMapping
    suspend fun create(@RequestBody createSectionRequest: CreateSectionRequest, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        sectionFlowService.create(managerId, createSectionRequest.courseId, createSectionRequest.sessionName)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping
    suspend fun getAll(@RequestParam("courseId") courseId: Long, session: WebSession): ResponseEntity<SessionsWithAttendanceResponse> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val allSessionAttendanceInfo = sectionFlowService.getAll(managerId, courseId)
        return ResponseEntity.ok(SessionsWithAttendanceResponse(allSessionAttendanceInfo))
    }

    data class CreateSectionRequest(
        val courseId: Long,
        val sessionName: String,
    )

    data class SessionsWithAttendanceResponse(
        val allSessionAttendanceInfo: List<SectionWithAttendanceDto>,
    )
}
