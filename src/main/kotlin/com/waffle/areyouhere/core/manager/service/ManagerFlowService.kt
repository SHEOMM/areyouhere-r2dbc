package com.waffle.areyouhere.core.manager.service

import com.waffle.areyouhere.core.email.EmailService
import com.waffle.areyouhere.core.email.model.MessageTemplate
import com.waffle.areyouhere.core.manager.model.EmailCodeRepository
import com.waffle.areyouhere.core.manager.model.Manager
import com.waffle.areyouhere.crossConcern.error.EmailNotSentYetException
import com.waffle.areyouhere.crossConcern.error.ManagerNotExistsException
import com.waffle.areyouhere.crossConcern.error.NotVerifiedCodeException
import com.waffle.areyouhere.crossConcern.error.VerificationCodeNotMatchedException
import com.waffle.areyouhere.util.AlphanumericIdGenerator
import com.waffle.areyouhere.util.EnvironmentService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ManagerFlowService(
    private val managerService: ManagerService,
    private val environmentService: EnvironmentService,
    private val alphanumericIdGenerator: AlphanumericIdGenerator,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService,
    private val emailCodeRepository: EmailCodeRepository,
) {
    @Transactional(readOnly = true)
    suspend fun login(email: String, password: String): Long? {
        val foundManager = managerService.findByEmail(email) ?: throw ManagerNotExistsException
        if (passwordEncoder.matches(password, foundManager.password)) {
            return foundManager.id!!
        }
        return null
    }

    @Transactional(readOnly = true)
    suspend fun throwIfEmailAlreadyExists(email: String) {
        managerService.throwIfAlreadyEmailUsed(email)
    }

    @Transactional
    suspend fun update(id: Long, nickname: String, password: String) {
        val manager = managerService.findById(id)
        manager.name = nickname
        manager.password = passwordEncoder.encode(password)
        managerService.save(manager)
    }

    @Transactional
    suspend fun signUp(email: String, password: String, nickname: String): Long {
        managerService.throwIfAlreadyEmailUsed(email)
        if (environmentService.isLocal().not()) {
            val code = emailCodeRepository.getOrNull(EmailCodeRepository.EmailCode.Key(email)) ?: throw EmailNotSentYetException
            if (code.verified.not()) throw NotVerifiedCodeException
        }

        val manager = Manager(
            name = nickname,
            email = email,
            password = passwordEncoder.encode(password),
        )
        managerService.save(manager)
        return manager.id!!
    }

    @Transactional(readOnly = true)
    suspend fun sendSignUpEmail(email: String) {
        managerService.throwIfAlreadyEmailUsed(email)
        val code = alphanumericIdGenerator.generate(codeLength)
        emailCodeRepository.set(
            EmailCodeRepository.EmailCode.Key(email),
            EmailCodeRepository.EmailCode.Value(code),
        )

        emailService.sendVerifyEmail(email, code, MessageTemplate.SIGN_UP)
    }

    @Transactional(readOnly = true)
    suspend fun verifyEmail(email: String, code: String) {
        val savedCode = emailCodeRepository.getOrNull(EmailCodeRepository.EmailCode.Key(email)) ?: throw EmailNotSentYetException
        if (code != savedCode.code) throw VerificationCodeNotMatchedException
        emailCodeRepository.set(
            EmailCodeRepository.EmailCode.Key(email),
            EmailCodeRepository.EmailCode.Value(code, true),
        )
    }

    private final val codeLength = 6
}
