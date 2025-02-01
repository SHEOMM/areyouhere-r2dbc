package com.waffle.areyouhere.core.email.model

data class MessageHolder(
    val title: String,
    val contents: String,
) {
    constructor(title: String, messageTemplate: MessageTemplate, values: Any?) : this(
        title,
        String.format(messageTemplate.template, values),
    )
}
