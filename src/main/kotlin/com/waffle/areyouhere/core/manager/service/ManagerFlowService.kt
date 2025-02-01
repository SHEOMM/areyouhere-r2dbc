package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.crossConcern.error.AlreadyExistsEmailException
import com.waffle.areyouhere.crossConcern.error.ManagerNotExistsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManagerFlowService(
    private val managerService: ManagerService,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional(readOnly = true)
    suspend fun login(email: String, password: String): Long? {
        val foundManager = managerService.findByEmail(email) ?: throw ManagerNotExistsException
        if (passwordEncoder.matches(password, foundManager.password)) {
            return foundManager.id!!
        }
        return null
    }

    suspend fun throwIfEmailAlreadyExists(email: String) {
        if (managerService.existsByEmail(email)) {
            throw AlreadyExistsEmailException
        }
    }

    @Transactional
    suspend fun update(id: Long, nickname: String, password: String) {
        val manager = managerService.findById(id)
        manager.name = nickname
        manager.password = passwordEncoder.encode(password)
        managerService.save(manager)
    }
}
