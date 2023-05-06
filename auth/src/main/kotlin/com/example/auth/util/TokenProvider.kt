package com.example.auth.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.model.entity.Member
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*
import java.util.stream.Collectors

class TokenProvider(
    @param:Value("\${jwt.secret}") private val SECRET: String,
    @Value("\${jwt.access-token-validity}") ACCESS_TOKEN_VALIDITY: Long,
    @Value("\${jwt.refresh-token-validity}") REFRESH_TOKEN_VALIDITY: Long,
) {
    private val accessTokenValidityMilliseconds: Long = ACCESS_TOKEN_VALIDITY * 1000000
    private val refreshTokenValidityMilliseconds: Long = REFRESH_TOKEN_VALIDITY * 1000000

    fun generateAccessToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        return JWT.create()
            .withSubject(authentication.name)
            .withExpiresAt(Date(Date().time + accessTokenValidityMilliseconds))
            .withClaim("auth", authorities)
            .sign(Algorithm.HMAC512(SECRET))
    }

    fun generateRefreshToken(authentication: Authentication, accessToken: String): String {
        return JWT.create()
            .withSubject(authentication.name)
            .withExpiresAt(Date(Date().time + refreshTokenValidityMilliseconds))
            .withClaim("accessToken", accessToken)
            .sign(Algorithm.HMAC512(SECRET))
    }
}
