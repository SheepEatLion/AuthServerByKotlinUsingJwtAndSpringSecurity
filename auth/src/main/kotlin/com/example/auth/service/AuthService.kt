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
        val authenticationToken = UsernamePasswordAuthenticationToken(generateTokenRequest.id, generateTokenRequest.email)
        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication = authentication

        val accessToken: String = tokenProvider.generateAccessToken(authentication = authentication)
        val refreshToken: String = tokenProvider.generateRefreshToken(authentication = authentication, accessToken = accessToken)

        val httpHeaders = HttpHeaders()
        httpHeaders.add("refresh-token", refreshToken)

        return ResponseEntity<AccessTokenResponse>(AccessTokenResponse(accessToken), httpHeaders, HttpStatus.OK)
    }

    fun invalidate() {

    }

    fun refresh() {

    }
}
