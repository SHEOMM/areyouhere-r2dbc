package com.waffle.areyouhere.crossConcern.error

import org.springframework.http.HttpStatus

enum class ErrorType(
    val httpStatus: HttpStatus,
    val errorCode: Long,
    val errorMessage: String,
    val displayMessage: String = "현재 서비스 이용이 원활하지 않습니다. 이용에 불편을 드려 죄송합니다.",
) {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 0x0000, "API 호출에 실패하였습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 0x0001, "잘못된 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 0x0003, "인증되지 않은 요청입니다."),
    RESPONSE_CONFLICT(HttpStatus.CONFLICT, 0x0004, "중복된 값이 존재합니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 0x0005, "존재하지 않는 값에 대한 요청입니다."),
}
