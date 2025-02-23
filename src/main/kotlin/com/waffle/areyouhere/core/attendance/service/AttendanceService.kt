package com.waffle.areyouhere.core.attendance.service

import com.waffle.areyouhere.core.attendance.repository.AttendanceRepository
import com.waffle.areyouhere.core.attendance.service.dto.AttendanceDto
import org.springframework.stereotype.Service

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
) {
    suspend fun getAllByAttendeeIdIn(attendanceIds: List<Long>): List<AttendanceDto> {
        return attendanceRepository.findByAttendeeIdIn(attendanceIds).map { AttendanceDto(it) }
    }

    suspend fun getBySectionId(sectionId: Long) = attendanceRepository.findBySectionId(sectionId).map { AttendanceDto(it) }
}
