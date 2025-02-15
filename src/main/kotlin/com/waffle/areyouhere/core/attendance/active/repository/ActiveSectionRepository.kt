package com.waffle.areyouhere.core.attendance.active.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.waffle.areyouhere.core.attendance.active.model.ActiveSection
import com.waffle.areyouhere.redis.CacheRepository
import com.waffle.areyouhere.redis.RedisRepository
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.stereotype.Component
import java.time.Duration

// Index 사용은 예외적인 구현이므로 보편화하지 않습니다.
@Component
class ActiveSectionRepository(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
    private val sectionSubKeyRepository: SectionSubKeyRepository,
    private val courseSubKeyRepository: CourseSubKeyRepository,
) : CacheRepository<ActiveSection.Key, ActiveSection.Value> by RedisRepository.create(
    reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
    objectMapper = objectMapper,
    defaultExpiry = Duration.ofDays(1),
    keyPrefix = "active_section",
) {
    suspend fun setWithIndex(key: ActiveSection.Key, value: ActiveSection.Value?, expiry: Duration?) {
        value?.let {
            sectionSubKeyRepository.set(ActiveSection.SectionSubKey(value.sectionId), ActiveSection.SectionSubValue(value.attendanceCode))
            courseSubKeyRepository.set(ActiveSection.CourseSubKey(value.courseId), ActiveSection.CourseSubValue(value.attendanceCode))
        }
        set(key, value, expiry)
    }

    suspend fun findByCourseId(courseId: Long): ActiveSection.Value? {
        return courseSubKeyRepository.getOrNull(ActiveSection.CourseSubKey(courseId))?.let {
            getOrNull(ActiveSection.Key(it.attendanceCode))
        }
    }
    suspend fun findBySectionId(sectionId: Long): ActiveSection.Value? {
        return sectionSubKeyRepository.getOrNull(ActiveSection.SectionSubKey(sectionId))?.let {
            getOrNull(ActiveSection.Key(it.attendanceCode))
        }
    }

    suspend fun deleteWithIndex(key: ActiveSection.Key): Long {
        val value = getOrNull(key)
        value?.let {
            sectionSubKeyRepository.delete(ActiveSection.SectionSubKey(value.sectionId))
            courseSubKeyRepository.delete(ActiveSection.CourseSubKey(value.courseId))
        }
        return delete(key)
    }
}

@Component
class SectionSubKeyRepository(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
) : CacheRepository<ActiveSection.SectionSubKey, ActiveSection.SectionSubValue> by RedisRepository.create(
    reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
    objectMapper = objectMapper,
    defaultExpiry = Duration.ofDays(1),
    keyPrefix = "active_section",
)

@Component
class CourseSubKeyRepository(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
) : CacheRepository<ActiveSection.CourseSubKey, ActiveSection.CourseSubValue> by RedisRepository.create(
    reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
    objectMapper = objectMapper,
    defaultExpiry = Duration.ofDays(1),
    keyPrefix = "active_section",
)
