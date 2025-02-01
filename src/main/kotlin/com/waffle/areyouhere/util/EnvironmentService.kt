package com.waffle.areyouhere.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EnvironmentService(
    @Value("\${environment}") private val environment: String,
) {
    fun isLocal(): Boolean {
        return environment == "local"
    }

    fun isTest(): Boolean {
        return environment == "dev"
    }

    fun isProd(): Boolean {
        return environment == "release"
    }
}
