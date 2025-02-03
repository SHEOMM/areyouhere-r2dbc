package com.waffle.areyouhere.core.attendee.repository

import com.waffle.areyouhere.core.attendee.model.Attendee
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendeeRepository : CoroutineCrudRepository<Attendee, Long> {
    suspend fun countAttendeesByCourseId(courseId: Long): Long
}
