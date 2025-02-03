package com.waffle.areyouhere.core.attendee.service

import com.waffle.areyouhere.core.attendee.model.Attendee
import com.waffle.areyouhere.core.attendee.repository.AttendeeBatchRepository
import com.waffle.areyouhere.core.attendee.repository.AttendeeRepository
import org.springframework.stereotype.Service

@Service
class AttendeeService(
    private val attendeeRepository: AttendeeRepository,
    private val attendeeBatchRepository: AttendeeBatchRepository,
) {

    fun isAttendeeUnique(attendeeNameAndNotes: List<String>): Boolean {
        return attendeeNameAndNotes.size == attendeeNameAndNotes.toSet().size
    }

    suspend fun insertBatch(attendees: List<Attendee>) {
        attendeeBatchRepository.insertBatch(attendees)
    }

    suspend fun countInCourse(courseId: Long): Long {
        return attendeeRepository.countAttendeesByCourseId(courseId)
    }
}
