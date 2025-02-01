package com.waffle.areyouhere.redis

interface CacheKey {
    fun toCacheKey(): String
}
