package com.waffle.areyouhere.redis

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.deleteAndAwait
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration
import kotlin.reflect.KClass

class RedisCache<K : CacheKey, V : Any>(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
    valueClass: KClass<V>,
    private val defaultExpiry: Duration,
    private val keyPrefix: String,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val redisTemplate = ReactiveRedisTemplate(
        reactiveRedisConnectionFactory,
        RedisSerializationContext.newSerializationContext<String, V>()
            .key(StringRedisSerializer())
            .hashKey(StringRedisSerializer())
            .value(JacksonJsonRedisSerializer(objectMapper, valueClass))
            .hashValue(JacksonJsonRedisSerializer(objectMapper, valueClass))
            .build(),
    )

    suspend fun set(key: K, value: V?, expiry: Duration = defaultExpiry) {
        try {
            if (value == null) redisTemplate.deleteAndAwait(key.toCacheKeyString())
            else {
                redisTemplate.opsForValue().setAndAwait(key.toCacheKeyString(), value, expiry)
            }
        } catch (e: Throwable) {
            logger.error(e.message, e)
        }
    }

    suspend fun get(key: K): V? {
        return try {
            redisTemplate.opsForValue()
                .getAndAwait(key.toCacheKeyString())
        } catch (e: Throwable) {
            logger.error(e.message, e)
            null
        }
    }

    private fun K.toCacheKeyString(): String {
        return "$keyPrefix:${this.toCacheKey()}"
    }

    companion object {
        inline fun <K : CacheKey, reified V : Any> create(
            reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
            objectMapper: ObjectMapper,
            defaultExpiry: Duration,
            keyPrefix: String,
        ): RedisCache<K, V> {
            return RedisCache(
                reactiveRedisConnectionFactory = reactiveRedisConnectionFactory,
                objectMapper = objectMapper,
                valueClass = V::class,
                defaultExpiry = defaultExpiry,
                keyPrefix = keyPrefix,
            )
        }
    }
    class JacksonJsonRedisSerializer<T : Any>(
        private val objectMapper: ObjectMapper,
        private val type: KClass<T>,
    ) : RedisSerializer<T> {
        override fun serialize(value: T?): ByteArray? {
            return objectMapper.writeValueAsBytes(value)
        }

        override fun deserialize(bytes: ByteArray?): T? = bytes?.let { objectMapper.readValue(it, type.java) }
    }
}
