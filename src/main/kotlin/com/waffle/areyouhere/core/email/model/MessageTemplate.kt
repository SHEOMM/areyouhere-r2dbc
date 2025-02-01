package com.waffle.areyouhere.core.email.model

enum class MessageTemplate(
    val template: String,
) {
    PASSWORD_RESET("You can reset password by this code: %s"),
    SIGN_UP("You can verify your email by this code: %s"),
}
