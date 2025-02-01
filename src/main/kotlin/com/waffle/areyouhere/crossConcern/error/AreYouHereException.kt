package com.waffle.areyouhere.crossConcern.error

open class AreYouHereException(
    val error: ErrorType = ErrorType.DEFAULT_ERROR,
    val errorMessage: String = error.errorMessage,
    val displayMessage: String = error.displayMessage,
) : RuntimeException(errorMessage)

object ManagerNotExistsException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "존재하지 않는 계정입니다.")
object UnAuthorizeException : AreYouHereException(ErrorType.UNAUTHORIZED, displayMessage = "로그인 후 재시도해주세요.")
object AlreadyExistsEmailException : AreYouHereException(ErrorType.RESPONSE_CONFLICT, displayMessage = "중복된 이메일이 있습니다. 다른 이메일로 시도해주세요.")
