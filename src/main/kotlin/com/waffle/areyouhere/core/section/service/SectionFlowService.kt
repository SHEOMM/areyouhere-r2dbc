package com.waffle.areyouhere.core.section.service

import com.waffle.areyouhere.core.attendance.active.service.ActiveSectionService
import com.waffle.areyouhere.core.attendee.service.AttendeeService
import com.waffle.areyouhere.core.course.service.CourseService
import com.waffle.areyouhere.core.section.service.dto.SectionSaveDto
import com.waffle.areyouhere.core.section.service.dto.SectionWithAttendanceDto
import com.waffle.areyouhere.crossConcern.error.ActivatedSessionExistsException
import com.waffle.areyouhere.crossConcern.error.AttendeeNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SectionFlowService(
    private val sectionService: SectionService,
    private val courseService: CourseService,
    private val activeSectionService: ActiveSectionService,
    private val attendeeService: AttendeeService,
    private val sectionAttendanceService: SectionAttendanceService,
) {
    @Transactional
    suspend fun create(managerId: Long, courseId: Long, sessionName: String) {
        courseService.get(courseId)
        sectionService.getMostRecent(courseId)?.let {
            if (activeSectionService.isActivated(it.id)) {
                throw ActivatedSessionExistsException
            }
        }
        if (attendeeService.countInCourse(courseId) == 0L) {
            throw AttendeeNotFoundException
        }
        val section = SectionSaveDto(
            courseId = courseId,
            name = sessionName,
        )
        sectionService.create(section)
    }

    @Transactional(readOnly = true)
    suspend fun getAll(managerId: Long, courseId: Long): List<SectionWithAttendanceDto> {
        courseService.get(courseId)
        return sectionAttendanceService.getSectionsWithAttendance(courseId)
    }
}
