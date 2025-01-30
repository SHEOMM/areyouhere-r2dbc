package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.core.manager.repository.ManagerRepository
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val managerRepository: ManagerRepository,
) {
    suspend fun findByEmail(email: String): Manager?{
        return managerRepository.findByEmail(email)
    }
}
