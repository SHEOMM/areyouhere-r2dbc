package com.waffle.areyouhere.crossConcern.session

import com.fasterxml.jackson.databind.ObjectMapper
import com.waffle.areyouhere.crossConcern.error.AreYouHereException
import com.waffle.areyouhere.crossConcern.error.ErrorType
import com.waffle.areyouhere.crossConcern.error.UnAuthorizeException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Metrics
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import org.springframework.web.server.WebSession
import org.springframework.web.util.pattern.PathPatternParser
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Component
class SessionFilter(
    private val meterRegistry: MeterRegistry,
    private val objectMapper: ObjectMapper,
) : WebFilter {

    private val logger = KotlinLogging.logger {}
    private val basePattern = PathPatternParser().parse("/api/**")
    private val excludePatterns = listOf(
        PathPatternParser().parse("/api/auth/login"),
        PathPatternParser().parse("/api/auth/signup"),
        PathPatternParser().parse("/api/auth//email-availability"),
        PathPatternParser().parse("/api/attendance"),
    )

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return measureLatency().flatMap {
            logRequest(exchange.request)
            processFilterLogic(exchange, chain)
        }.onErrorResume { handleFilterError(it, exchange) }
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
        if (session.getAttribute<Any>("user") == null) {
            throw UnAuthorizeException
        }
        meterRegistry.counter("session.validation.success").increment()
    }

    private fun shouldApplyFilter(exchange: ServerWebExchange): Boolean {
        val path = exchange.request.path.pathWithinApplication()
        return basePattern.matches(path) && excludePatterns.none { it.matches(path) }
    }

    private fun logRequest(request: ServerHttpRequest) {
        logger.info {
            "${request.headers["X-Forwarded-For"]?.firstOrNull() ?: request.remoteAddress?.hostName} " +
                "${request.method.name()} ${request.uri.path}"
        }
    }

    private fun handleFilterError(error: Throwable, exchange: ServerWebExchange): Mono<Void> {
        val (httpStatus, errorBody) = when (error) {
            is AreYouHereException -> error.error.httpStatus to makeErrorBody(error)
            is ResponseStatusException -> error.statusCode to makeErrorBody(
                AreYouHereException(errorMessage = error.body.title ?: ErrorType.DEFAULT_ERROR.errorMessage),
            )
            else -> {
                logger.error { "Unexpected error $error" }
                HttpStatus.INTERNAL_SERVER_ERROR to makeErrorBody(AreYouHereException())
            }
        }
        return exchange.respondWith(httpStatus, errorBody)
    }

    private fun makeErrorBody(exception: AreYouHereException) =
        ErrorBody(exception.error.errorCode, exception.errorMessage, exception.displayMessage)

    private fun ServerWebExchange.respondWith(status: HttpStatusCode, errorBody: ErrorBody): Mono<Void> {
        response.statusCode = status
        response.headers.contentType = MediaType.APPLICATION_JSON
        val buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorBody))
        return response.writeWith(Mono.just(buffer))
    }

    private fun measureLatency() = Mono.fromCallable { System.nanoTime() }
        .doOnNext { start ->
            Metrics.timer("filter.latency")
                .record(System.nanoTime() - start, TimeUnit.NANOSECONDS)
        }
}

private data class ErrorBody(
    val errcode: Long,
    val message: String,
    val displayMessage: String,
)
