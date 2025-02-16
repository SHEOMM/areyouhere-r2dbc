package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.section.service.SectionFlowService
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/{sectionId}")
    suspend fun get(@PathVariable sectionId: Long, session: WebSession): ResponseEntity<SectionWithAttendanceDto> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val sectionWithAttendanceDto = sectionFlowService.getWithAttendance(managerId, sectionId)
        return ResponseEntity.ok(sectionWithAttendanceDto)
    }

    @GetMapping
    suspend fun getAll(@RequestParam("courseId") courseId: Long, session: WebSession): ResponseEntity<SectionsWithAttendanceResponse> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        val allSessionAttendanceInfo = sectionFlowService.getAllWithAttendance(managerId, courseId)
        return ResponseEntity.ok(SectionsWithAttendanceResponse(allSessionAttendanceInfo))
    }

    @PostMapping("/delete")
    suspend fun deleteAll(@RequestBody deleteSectionsRequest: DeleteSectionsRequest, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        sectionFlowService.deleteAll(managerId, deleteSectionsRequest.sessionIds)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping
    suspend fun deleteNotActivated(@RequestParam("courseId") courseId: Long, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        sectionFlowService.deleteNotActivated(managerId, courseId)
        return ResponseEntity(HttpStatus.OK)
    }

    data class CreateSectionRequest(
        val courseId: Long,
        val sessionName: String,
    )

    data class SectionsWithAttendanceResponse(
        val allSessionAttendanceInfo: List<SectionWithAttendanceDto>,
    )

    data class DeleteSectionsRequest(
        val sessionIds: List<Long>,
    )
}
