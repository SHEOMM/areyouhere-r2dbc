package com.waffle.areyouhere.core.manager.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.waffle.areyouhere.redis.CacheKey
import com.waffle.areyouhere.redis.CacheRepository
import com.waffle.areyouhere.redis.RedisRepository
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class EmailCodeRepository(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
) : CacheRepository<EmailCodeRepository.EmailCode.Key, EmailCodeRepository.EmailCode.Value> by RedisRepository.create(
    reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
    objectMapper = objectMapper,
    defaultExpiry = Duration.ofMinutes(5),
    keyPrefix = "email_verification",
) {

    class EmailCode {
        class Key(
            val email: String,
        ) : CacheKey {
            override fun toCacheKey(): String {
                return email
            }
        }

        data class Value(
            val code: String,
            val verified: Boolean = false,
        )
    }
}
