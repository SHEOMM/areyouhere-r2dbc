package com.waffle.areyouhere.crossConcern.error

open class AreYouHereException(
    val error: ErrorType = ErrorType.DEFAULT_ERROR,
    val errorMessage: String = error.errorMessage,
    val displayMessage: String = error.displayMessage,
) : RuntimeException(errorMessage)

object ManagerNotExistsException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "존재하지 않는 이메일입니다.")
