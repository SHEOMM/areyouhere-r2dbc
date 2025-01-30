package com.waffle.areyouhere.core.manager.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("manager")
data class Manager(
    @Id
    var id: Long? = null,
    var email: String,
    var name: String,
    var password: String,
)
