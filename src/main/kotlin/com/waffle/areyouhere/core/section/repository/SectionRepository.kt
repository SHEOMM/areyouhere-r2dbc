package com.waffle.areyouhere.core.section.repository

import com.waffle.areyouhere.core.section.model.Section
import com.waffle.areyouhere.core.section.repository.dto.SectionWithAttendance
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SectionRepository : CoroutineCrudRepository<Section, Long> {
    suspend fun findFirstByCourseIdOrderByCreatedAtDesc(courseId: Long): Section?

    @Query(
        value = "SELECT session.id as id, session.auth_code_created_at as date, session.name as name, " +
            "COUNT(case when attendance.status = 'ATTENDED' then 1 end) as attendee, " +
            "COUNT(case when attendance.status = 'ABSENT' then 1 end) as absentee," +
            "COUNT(case when attendance.status = 'LATE' then 1 end) as late \n" +
            "FROM session " +
            "INNER JOIN attendance ON session.id = attendance.session_id \n" +
            "WHERE session.course_id = :courseId \n" +
            "GROUP BY session.id \n" +
            "ORDER BY date ASC",
    )
    suspend fun findAllWithAttendanceByCourseId(courseId: Long): List<SectionWithAttendance>
}
