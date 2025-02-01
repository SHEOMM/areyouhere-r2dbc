package com.waffle.areyouhere.util

import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class AlphanumericIdGenerator {
    fun generate(length: Int): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder(length)

        repeat(length) {
            sb.append(chars[Random.nextInt(chars.length)])
        }

        return sb.toString()
    }
}
