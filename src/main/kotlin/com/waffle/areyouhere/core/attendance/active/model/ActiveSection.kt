package com.waffle.areyouhere.core.attendance.active.model

import com.waffle.areyouhere.core.attendance.model.AttendanceType
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeNameNoteDto
import com.waffle.areyouhere.redis.CacheKey
import java.time.Instant

class ActiveSection {
    class Key(
        val attendanceCode: String,
    ) : CacheKey {
        override fun toCacheKey(): String {
            return attendanceCode
        }
    }

    data class Value(
        val attendanceCode: String,
        val sectionId: Long,
        val courseId: Long,
        val courseName: String,
        val sectionName: String,
        val attendees: List<AttendeeNameNoteDto>,
        val attendances: Map<Long, AttendanceTime>,
        val attendanceStatus: AttendanceType,
        val createdAt: Instant,
    )

    class SectionSubKey(
        val sectionId: Long,
    ) : CacheKey {
        override fun toCacheKey(): String {
            return sectionId.toString()
        }
    }
    data class SectionSubValue(
        val attendanceCode: String,
    )

    class CourseSubKey(
        val courseId: Long,
    ) : CacheKey {
        override fun toCacheKey(): String {
            return courseId.toString()
        }
    }
    data class CourseSubValue(
        val attendanceCode: String,
    )
}
