package com.waffle.areyouhere.core.attendee.repository

import com.waffle.areyouhere.core.attendee.model.Attendee
import com.waffle.areyouhere.util.bindNullable
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.time.Instant

@Repository
class AttendeeBatchRepository(
    private val databaseClient: DatabaseClient,
) {
    private val logger = KotlinLogging.logger {}
    suspend fun insertBatch(attendees: List<Attendee>) {
        databaseClient.inConnectionMany { connection ->
            val statement =
                connection.createStatement("INSERT INTO attendee(name, note, course_id, created_at, updated_at) VALUES ($1, $2, $3, $4, $5)")
            val addLimit = attendees.size - 1
            attendees.forEachIndexed { index, attendee ->
                statement
                    .bind(0, attendee.name)
                    .bindNullable(1, attendee.note, String::class.java)
                    .bind(2, attendee.courseId)
                    .bind(3, Instant.now())
                    .bind(4, Instant.now())
                if (index < addLimit)
                    statement.add()
            }
            statement.fetchSize(attendees.size)

            Flux.from(statement.execute())
        }.awaitLast()
    }
}
