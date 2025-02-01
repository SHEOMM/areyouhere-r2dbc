package com.waffle.areyouhere.api

import com.waffle.areyouhere.core.manager.service.ManagerFlowService
import com.waffle.areyouhere.core.manager.service.ManagerSignUpApiService
import com.waffle.areyouhere.core.session.SessionInfo
import com.waffle.areyouhere.core.session.SessionManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession

@RestController
@RequestMapping("/api/auth")
class ManagerController(
    private val managerFlowService: ManagerFlowService,
    private val managerSignUpApiService: ManagerSignUpApiService,
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
        val managerId = managerSignUpApiService.signUp(signUpRequestDTO.email, signUpRequestDTO.password, signUpRequestDTO.password)
        sessionManager.set(session, SessionInfo(managerId))

        return ResponseEntity.status(HttpStatus.OK)
            .build()
    }
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
