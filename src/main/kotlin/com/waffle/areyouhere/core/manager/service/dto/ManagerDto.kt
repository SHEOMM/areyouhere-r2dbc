package com.waffle.areyouhere.core.manager.service.dto

import com.waffle.areyouhere.core.manager.model.Manager

data class ManagerDto(
    val email: String,
    val name: String,
) {
    constructor(manager: Manager) : this(
        email = manager.email,
        name = manager.name,
    )
}
