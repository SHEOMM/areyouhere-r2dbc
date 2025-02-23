package com.waffle.areyouhere.core.attendee.service

import com.waffle.areyouhere.core.attendance.model.AttendanceType
import com.waffle.areyouhere.core.attendance.service.AttendanceService
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeSaveDto
import com.waffle.areyouhere.core.attendee.service.dto.AttendeeWithAttendanceCountDto
import com.waffle.areyouhere.core.course.service.CourseService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AttendeeFlowService(
    private val attendeeService: AttendeeService,
    private val attendanceService: AttendanceService,
    private val courseService: CourseService,
) {
    @Transactional(readOnly = true)
    suspend fun getAllInCourseWithAttendance(managerId: Long, courseId: Long): List<AttendeeWithAttendanceCountDto> {
        courseService.getWithManagerId(courseId, managerId)
        val attendeesInCourse = attendeeService.getByCourseId(courseId)
        val attendanceGroupByAttendeeId = attendanceService.getAllByAttendeeIdIn(attendeesInCourse.map { it.id })
            .groupBy { it.attendeeId }
        return attendeesInCourse.map {
            AttendeeWithAttendanceCountDto(
                attendee = AttendeeSaveDto(it),
                attendance = attendanceGroupByAttendeeId[it.id]?.count { it.status == AttendanceType.ATTENDED } ?: 0,
                absence = attendanceGroupByAttendeeId[it.id]?.count { it.status == AttendanceType.ABSENT } ?: 0,
                late = attendanceGroupByAttendeeId[it.id]?.count { it.status == AttendanceType.LATE } ?: 0,
            )
        }
    }
}
