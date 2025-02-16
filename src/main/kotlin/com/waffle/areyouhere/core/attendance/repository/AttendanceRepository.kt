package com.waffle.areyouhere.core.attendance.repository

import com.waffle.areyouhere.core.attendance.model.Attendance
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendanceRepository : CoroutineCrudRepository<Attendance, Long> {
    suspend fun findBySectionId(sectionId: Long): List<Attendance>
}
