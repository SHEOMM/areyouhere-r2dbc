package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.crossConcern.error.AlreadyExistsEmailException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManagerSignUpApiService(
    private val managerService: ManagerService,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    suspend fun signUp(email: String, password: String, nickname: String): Long {
        if (managerService.existsByEmail(email)) throw AlreadyExistsEmailException
        val manager = Manager(
            name = nickname,
            email = email,
            password = passwordEncoder.encode(password),
        )
        managerService.save(manager)
        return manager.id!!
    }
}
