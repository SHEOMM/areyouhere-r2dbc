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

class RedisRepository<K : CacheKey, V : Any>(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    objectMapper: ObjectMapper,
    valueClass: KClass<V>,
    private val defaultExpiry: Duration,
    private val keyPrefix: String,
) : CacheRepository<K, V> {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val redisTemplate = ReactiveRedisTemplate(
        reactiveRedisConnectionFactory,
        RedisSerializationContext.newSerializationContext<String, V>()
            .key(StringRedisSerializer())
            .hashKey(StringRedisSerializer())
            .value(JacksonJsonRedisSerializer(objectMapper, valueClass))
            .hashValue(JacksonJsonRedisSerializer(objectMapper, valueClass))
            .build(),
    )

    override suspend fun set(key: K, value: V?, expiry: Duration?) {
        try {
            if (value == null) redisTemplate.deleteAndAwait(key.toCacheKeyString())
            else {
                redisTemplate.opsForValue().setAndAwait(key.toCacheKeyString(), value, expiry ?: defaultExpiry)
            }
        } catch (e: Throwable) {
            logger.error(e.message, e)
        }
    }

    override suspend fun getOrNull(key: K): V? {
        return try {
            redisTemplate.opsForValue()
                .getAndAwait(key.toCacheKeyString())
        } catch (e: Throwable) {
            logger.error(e.message, e)
            null
        }
    }

    override suspend fun delete(key: K): Long {
        return try {
            val cacheKey = key.toCacheKeyString()

            redisTemplate.deleteAndAwait(cacheKey)
        } catch (t: Throwable) {
            logger.error(t.message, t)
            0
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
        ): RedisRepository<K, V> {
            return RedisRepository(
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
