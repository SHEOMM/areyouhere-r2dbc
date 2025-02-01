package com.waffle.areyouhere.config

import org.springframework.context.annotation.Configuration
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession

@Configuration(proxyBeanMethods = false)
@EnableRedisWebSession
class SessionConfig
