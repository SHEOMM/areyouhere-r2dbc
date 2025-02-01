package com.waffle.areyouhere.core.manager.repository

import com.waffle.areyouhere.core.manager.model.Manager
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ManagerRepository : CoroutineCrudRepository<Manager, Long> {
    suspend fun findByEmail(email: String): Manager?
}
