package com.example.auth.service

import com.example.auth.model.dto.AccessTokenResponse
import com.example.auth.model.dto.GenerateTokenRequest
import com.example.auth.util.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    val tokenProvider: TokenProvider,
    val authenticationManagerBuilder: AuthenticationManagerBuilder,
) {
    fun generate(generateTokenRequest: GenerateTokenRequest): ResponseEntity<AccessTokenResponse> {
        setSecurityHolder(id = generateTokenRequest.id, email = generateTokenRequest.email)
        val accessToken: String = tokenProvider.generateAccessToken(authentication = SecurityContextHolder.getContext().authentication)
        val refreshToken: String = tokenProvider.generateRefreshToken(authentication = SecurityContextHolder.getContext().authentication, email = generateTokenRequest.email)

        val httpHeaders = HttpHeaders()
        httpHeaders.add("refresh-token", refreshToken)

        return ResponseEntity<AccessTokenResponse>(AccessTokenResponse(accessToken), httpHeaders, HttpStatus.OK)
    }

    fun invalidate() {
        //TODO 토큰 무효화 (강제 로그아웃)
    }

    fun refresh(refreshToken: String): ResponseEntity<AccessTokenResponse> {
        tokenProvider.validate(token = refreshToken)
        setSecurityHolder(tokenProvider.getId(refreshToken = refreshToken), tokenProvider.getEmail(refreshToken = refreshToken))

        val accessToken: String = tokenProvider.generateAccessToken(authentication = SecurityContextHolder.getContext().authentication)

        return ResponseEntity<AccessTokenResponse>(AccessTokenResponse(accessToken), HttpStatus.OK)
    }

    fun setSecurityHolder(id: Long, email: String) {
        val authenticationToken = UsernamePasswordAuthenticationToken(id, email)
        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication = authentication
    }
}
