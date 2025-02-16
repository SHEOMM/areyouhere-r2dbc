package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.section.repository.SectionRepository
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import org.springframework.stereotype.Service

@Service
class SectionAttendanceService(
    private val sectionRepository: SectionRepository,
) {
    suspend fun getSectionsWithAttendance(courseId: Long): List<SectionWithAttendanceDto> {
        return sectionRepository.findAllWithAttendanceByCourseId(courseId)
            .map { SectionWithAttendanceDto(it) }
    }
}
