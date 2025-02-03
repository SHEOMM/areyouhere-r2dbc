package com.waffle.areyouhere.util

import io.r2dbc.spi.Statement

fun Statement.bindNullable(index: Int, value: Any?, type: Class<*>) = apply {
    if (value != null) {
        bind(index, value)
    } else {
        bindNull(index, type)
    }
}
