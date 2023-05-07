package com.example.auth.controller

import com.example.auth.model.dto.AccessTokenResponse
import com.example.auth.model.dto.GenerateTokenRequest
import com.example.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("v1/auth")
@RestController
class AuthController(
    val authService: AuthService,
) {
    @PostMapping("/token/generate")
    fun generate(@RequestBody @Valid generateTokenRequest: GenerateTokenRequest): ResponseEntity<AccessTokenResponse> {
        return authService.generate(generateTokenRequest)
    }

    @PostMapping("/token/invalidate")
    fun invalidate() {

    }

    @PostMapping("/token/refresh")
    fun refresh() {

    }
}
