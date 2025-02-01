package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.manager.service.ManagerFlowService
import com.waffle.areyouhere.core.session.SessionInfo
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/auth")
class ManagerController(
    private val managerFlowService: ManagerFlowService,
    private val sessionManager: SessionManager,
) {
    @PostMapping("/login")
    suspend fun login(@RequestBody loginRequestDto: LoginRequestDTO, session: WebSession): ResponseEntity<HttpStatus> {
        managerFlowService.login(email = loginRequestDto.email, password = loginRequestDto.password)?.let {
            sessionManager.set(session, SessionInfo(it))
            return ResponseEntity.status(HttpStatus.OK)
                .build()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }

    @PostMapping("signup")
    suspend fun signUp(@RequestBody signUpRequestDTO: SignUpRequestDTO, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = managerFlowService.signUp(signUpRequestDTO.email, signUpRequestDTO.password, signUpRequestDTO.password)
        sessionManager.set(session, SessionInfo(managerId))

        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @GetMapping("logout")
    suspend fun logout(session: WebSession): ResponseEntity<HttpStatus> {
        sessionManager.remove(session)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/email-availability")
    suspend fun checkDuplicatedEmail(@RequestParam email: String): ResponseEntity<HttpStatus> {
        managerFlowService.throwIfEmailAlreadyExists(email)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @DeleteMapping
    suspend fun delete(session: WebSession): ResponseEntity<HttpStatus> {
        sessionManager.login().getManagerIdOrThrow(session)
        sessionManager.remove(session)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @PutMapping
    suspend fun update(@RequestBody updateRequestDto: UpdateRequestDto, session: WebSession): ResponseEntity<HttpStatus> {
        val managerId = sessionManager.login().getManagerIdOrThrow(session)
        managerFlowService.update(managerId, updateRequestDto.nickname, updateRequestDto.password)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @GetMapping("/email")
    suspend fun sendSignUpEmail(@RequestParam email: String): ResponseEntity<HttpStatus> {
        managerFlowService.sendSignUpEmail(email)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    @PostMapping("/verification")
    suspend fun verifyEmail(@RequestBody verifyEmailRequestDto: VerifyEmailRequestDto): ResponseEntity<HttpStatus> {
        managerFlowService.verifyEmail(verifyEmailRequestDto.email, verifyEmailRequestDto.code)
        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }

    data class LoginRequestDTO(
        val email: String,
        val password: String,
    )

    data class SignUpRequestDTO(
        val email: String,
        val password: String,
        val nickname: String,
    )

    data class UpdateRequestDto(
        val nickname: String,
        val password: String,
    )

    data class VerifyEmailRequestDto(
        val code: String,
        val email: String,
    )
}
