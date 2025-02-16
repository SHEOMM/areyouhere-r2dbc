package com.waffle.areyouhere.core.section.repository.dto

import java.time.LocalDateTime

interface SectionWithAttendance {
    fun getid(): Long?
    fun getdate(): LocalDateTime?
    fun getname(): String?
    fun getattendee(): Int
    fun getlate(): Int
    fun getabsentee(): Int
}
