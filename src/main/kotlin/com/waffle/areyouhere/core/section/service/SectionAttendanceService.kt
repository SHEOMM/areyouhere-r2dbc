package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.attendance.service.AttendanceService
import com.waffle.areyouhere.core.section.service.dto.SectionDto
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import org.springframework.stereotype.Service

@Service
class SectionAttendanceService(
    private val sectionService: SectionService,
    private val attendanceService: AttendanceService,
) {
    suspend fun getSectionsWithAttendance(courseId: Long): List<SectionWithAttendanceDto> {
        return sectionService.getAllWithAttendances(courseId)
    }

    suspend fun getSectionWithAttendance(sectionDto: SectionDto): SectionWithAttendanceDto {
        val attendances = attendanceService.findBySectionId(sectionDto.id)
        return SectionWithAttendanceDto(sectionDto, attendances)
    }
}
