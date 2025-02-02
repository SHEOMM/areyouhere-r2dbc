package com.waffle.areyouhere.crossConcern.session

import com.waffle.areyouhere.core.session.SessionManager.Companion.SESSION_KEY
import com.waffle.areyouhere.crossConcern.error.UnAuthorizeException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import org.springframework.core.annotation.Order
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.WebSession
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Order(Integer.MIN_VALUE)
@Component
class SessionFilter(
    private val meterRegistry: MeterRegistry,
) : WebFilter {

    private val logger = KotlinLogging.logger {}
    private val basePattern = PathPatternParser().parse("/api/**")

    // FIXME: 프론트와 url 통일해서 regexp로 대응
    private val excludePatterns = listOf(
        PathPatternParser().parse("/api/auth/login"),
        PathPatternParser().parse("/api/auth/signup"),
        PathPatternParser().parse("/api/auth/email"),
        PathPatternParser().parse("/api/auth/email-availability"),
        PathPatternParser().parse("/api/auth/verification"),
        PathPatternParser().parse("/api/attendance"),
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return measureLatency().flatMap {
            logRequest(exchange.request)
            processFilterLogic(exchange, chain)
        }
    }

    private fun processFilterLogic(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return when {
            shouldApplyFilter(exchange) -> validateSession(exchange).then(chain.filter(exchange))
            else -> chain.filter(exchange)
        }
    }

    private fun validateSession(exchange: ServerWebExchange): Mono<WebSession> {
        return exchange.session.flatMap { session ->
            checkSessionAttributes(session)
                .let { Mono.just(session) }
        }.doOnError { meterRegistry.counter("session.validation.failure").increment() }
    }

    private fun checkSessionAttributes(session: WebSession) {
        if (session.getAttribute<Any>(SESSION_KEY) == null) {
            throw UnAuthorizeException
        }
        meterRegistry.counter("session.validation.success").increment()
    }

    private fun shouldApplyFilter(exchange: ServerWebExchange): Boolean {
        val isOptionsMethod = exchange.request.method == org.springframework.http.HttpMethod.OPTIONS
        val path = exchange.request.path.pathWithinApplication()
        return basePattern.matches(path) && excludePatterns.none { it.matches(path) } && !isOptionsMethod
    }

    private fun logRequest(request: ServerHttpRequest) {
        logger.info {
            "${request.headers["X-Forwarded-For"]?.firstOrNull() ?: request.remoteAddress?.hostName} " +
                "${request.method.name()} ${request.uri.path}"
        }
    }

    private fun measureLatency() = Mono.fromCallable { System.nanoTime() }
        .doOnNext { start ->
            Metrics.timer("filter.latency")
                .record(System.nanoTime() - start, TimeUnit.NANOSECONDS)
        }
}
