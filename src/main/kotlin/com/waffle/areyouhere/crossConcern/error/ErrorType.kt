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
    EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, 0x0002, "존재하지 않는 이메일입니다."),
}
