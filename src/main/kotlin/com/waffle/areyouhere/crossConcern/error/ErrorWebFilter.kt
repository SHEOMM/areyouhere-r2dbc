package com.waffle.areyouhere.crossConcern.error

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.netty.channel.AbortedException

@Order(Integer.MIN_VALUE)
@Component
class ErrorWebFilter(
    private val objectMapper: ObjectMapper,
) : WebFilter {

    private val logger = KotlinLogging.logger {}

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> =
        chain.filter(exchange).onErrorResume { throwable ->
            val (httpStatus, errorBody) = when (throwable) {
                is AreYouHereException -> throwable.error.httpStatus to makeErrorBody(throwable)
                is ResponseStatusException -> {
                    logger.info { "Unexpected error $throwable" }
                    logger.info { throwable.printStackTrace() }
                    throwable.statusCode to makeErrorBody(
                        AreYouHereException(errorMessage = throwable.body.title ?: ErrorType.DEFAULT_ERROR.errorMessage),
                    )
                }
                is AbortedException -> HttpStatus.GATEWAY_TIMEOUT to makeErrorBody(AreYouHereException())
                else -> {
                    // TODO: 슬랙 메시지 전송
                    logger.error { "Unexpected error $throwable" }
                    logger.error { throwable.printStackTrace() }
                    HttpStatus.INTERNAL_SERVER_ERROR to makeErrorBody(AreYouHereException())
                }
            }
            exchange.respondWith(httpStatus, errorBody)
        }

    private fun makeErrorBody(exception: AreYouHereException) =
        ErrorBody(exception.error.errorCode, exception.errorMessage, exception.displayMessage)

    private fun ServerWebExchange.respondWith(status: HttpStatusCode, errorBody: ErrorBody): Mono<Void> {
        response.statusCode = status
        response.headers.contentType = MediaType.APPLICATION_JSON
        val buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(errorBody))
        return response.writeWith(Mono.just(buffer))
    }
}

private data class ErrorBody(
    val errcode: Long,
    val message: String,
    val displayMessage: String,
)
