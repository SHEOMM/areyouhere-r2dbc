package com.waffle.areyouhere.core.attendee.service

import com.waffle.areyouhere.core.attendee.model.Attendee
import com.waffle.areyouhere.core.attendee.repository.AttendeeBatchRepository
import com.waffle.areyouhere.core.attendee.repository.AttendeeRepository
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeDto
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeSaveDto
import org.springframework.stereotype.Service

@Service
class AttendeeService(
    private val attendeeRepository: AttendeeRepository,
    private val attendeeBatchRepository: AttendeeBatchRepository,
) {
    suspend fun insertBatch(attendees: List<AttendeeSaveDto>) {
        attendeeBatchRepository.insertBatch(attendees.map { Attendee(it) })
    }

    suspend fun getByCourseId(courseId: Long): List<AttendeeDto> {
        return attendeeRepository.findByCourseId(courseId).map { AttendeeDto(it) }
    }

    fun isAttendeeUnique(attendeeNameAndNotes: List<String>): Boolean {
        return attendeeNameAndNotes.size == attendeeNameAndNotes.toSet().size
    }

    suspend fun countInCourse(courseId: Long): Long {
        return attendeeRepository.countAttendeesByCourseId(courseId)
    }
}
