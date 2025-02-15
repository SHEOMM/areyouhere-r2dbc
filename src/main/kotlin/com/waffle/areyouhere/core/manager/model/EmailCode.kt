package com.waffle.areyouhere.core.manager.model

import com.waffle.areyouhere.redis.CacheKey

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
