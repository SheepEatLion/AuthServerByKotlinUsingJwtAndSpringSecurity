package com.example.auth.controller

import com.example.auth.model.dto.AccessTokenResponse
import com.example.auth.model.dto.GetKakaoUserEmailRequest
import com.example.auth.model.dto.GetKakaoUserEmailResponse
import com.example.auth.model.dto.GenerateTokenRequest
import com.example.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
        // TODO
    }

    @PostMapping("/token/refresh")
    fun refresh(@CookieValue("refresh-token") refreshToken: String): ResponseEntity<AccessTokenResponse> {
        return authService.refresh(refreshToken = refreshToken)
    }

    @PostMapping("/kakao")
    fun getKakaoUser(@RequestBody @Valid getKakaoUserEmailRequest: GetKakaoUserEmailRequest): GetKakaoUserEmailResponse {
        return authService.getKakaoUser(getKakaoUserEmailRequest)
    }
}
