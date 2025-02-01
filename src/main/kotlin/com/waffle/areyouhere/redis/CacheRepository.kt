package com.waffle.areyouhere.redis

import java.time.Duration

interface CacheRepository<K : Any?, V : Any> {
    suspend fun set(key: K, value: V?, expiry: Duration? = null)
    suspend fun getOrNull(key: K): V?
}
