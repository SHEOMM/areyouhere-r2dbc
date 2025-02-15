package com.waffle.areyouhere.core.manager.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.waffle.areyouhere.core.manager.model.EmailCode
import com.waffle.areyouhere.redis.CacheRepository
import com.waffle.areyouhere.redis.RedisRepository
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class EmailCodeRepository(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
) : CacheRepository<EmailCode.Key, EmailCode.Value> by RedisRepository.create(
    reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
    objectMapper = objectMapper,
    defaultExpiry = Duration.ofMinutes(5),
    keyPrefix = "email_verification",
)
