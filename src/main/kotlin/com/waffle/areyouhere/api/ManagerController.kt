package com.waffle.areyouhere.api

import com.waffle.areyouhere.api.dto.LoginRequestDTO
import com.waffle.areyouhere.core.manager.service.ManagerFlowService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class ManagerController(
    private val managerFlowService: ManagerFlowService,
) {
    @PostMapping("login")
    suspend fun login(@RequestBody loginRequestDto: LoginRequestDTO): ResponseEntity<HttpStatus> {
        if (managerFlowService.login(email = loginRequestDto.email, password = loginRequestDto.password)) {
            return ResponseEntity.status(HttpStatus.OK).build()
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }
}
