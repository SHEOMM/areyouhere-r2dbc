package com.waffle.areyouhere.crossConcern.error

open class AreYouHereException(
    val error: ErrorType = ErrorType.DEFAULT_ERROR,
    val errorMessage: String = error.errorMessage,
    val displayMessage: String = error.displayMessage,
) : RuntimeException(errorMessage)

object ManagerNotExistsException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "존재하지 않는 계정입니다.")
object UnAuthorizeException : AreYouHereException(ErrorType.UNAUTHORIZED, displayMessage = "로그인 후 재시도해주세요.")
object AlreadyExistsEmailException : AreYouHereException(ErrorType.RESPONSE_CONFLICT, displayMessage = "중복된 이메일이 있습니다. 다른 이메일로 시도해주세요.")
object EmailNotSentYetException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "이메일을 먼저 보낸 후 인증을 시도해주세요.")
object VerificationCodeNotMatchedException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "코드가 일치하지 않습니다.")
object NotVerifiedCodeException : AreYouHereException(ErrorType.UNAUTHORIZED, displayMessage = "코드를 먼저 인증해주세요.")
object AttendeeNotUniqueException : AreYouHereException(ErrorType.RESPONSE_CONFLICT, displayMessage = "중복되는 수강자의 이름의 note를 변경해주세요.")
object CourseNotFoundException : AreYouHereException(ErrorType.NOT_FOUND, displayMessage = "존재하지 않는 강의입니다.")
object ActivatedSessionExistsException : AreYouHereException(ErrorType.RESPONSE_CONFLICT, displayMessage = "이미 활성화된 세션이 존재합니다.")
object AttendeeNotFoundException : AreYouHereException(ErrorType.BAD_REQUEST, displayMessage = "수강 인원이 존재하지 않습니다.")
object SectionNotFoundException : AreYouHereException(ErrorType.NOT_FOUND, displayMessage = "존재하지 않는 세션입니다.")
