package com.waffle.areyouhere.core.attendee.repository

import com.waffle.areyouhere.core.attendee.model.Attendee
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Instant

@Repository
class AttendeeBatchRepository(
    private val databaseClient: DatabaseClient,
) {
    suspend fun insertBatch(attendees: List<Attendee>) {
        databaseClient.inConnectionMany { connection ->
            val statement =
                connection.createStatement("INSERT INTO attendee(name, note, course_id, created_at, updated_at) VALUES ($1, $2, $3, $4, $5)")
            for (attendee in attendees) {
                attendee.note?.let { statement.bind(0, attendee.name).bind(1, it).bind(2, attendee.courseId).bind(3, Instant.now()).bind(4, Instant.now()) }
                    ?: statement.bind(0, attendee.name).bind(2, attendee.courseId).bind(3, Instant.now()).bind(4, Instant.now())
            }
            Flux.from(statement.execute())
        }.awaitLast()
    }
}
