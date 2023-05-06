package com.example.auth.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.model.entity.Member
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import java.security.Key
import java.util.*

class TokenProvider(
    @param:Value("\${jwt.secret}") private val SECRET: String,
    @Value("\${jwt.access-token-validity}") ACCESS_TOKEN_VALIDITY: Long,
    @Value("\${jwt.refresh-token-validity}") REFRESH_TOKEN_VALIDITY: Long,
) {
    private val accessTokenValidityMilliseconds: Long = ACCESS_TOKEN_VALIDITY * 1000000
    private val refreshTokenValidityMilliseconds: Long = REFRESH_TOKEN_VALIDITY * 1000000

    fun generateAccessToken(member: Member): String {
        return JWT.create()
            .withSubject(member.name)
            .withExpiresAt(Date(Date().time + accessTokenValidityMilliseconds))
            .withClaim("id", member.id)
            .withClaim("auth", authorities)
            .sign(Algorithm.HMAC512(SECRET))
    }

    fun generateRefreshToken(member: Member, accessToken: String): String {
        return JWT.create()
            .withSubject()
            .withExpiresAt(Date(Date().time + refreshTokenValidityMilliseconds))
            .withClaim("accessToken", accessToken)
            .withClaim("auth", authorities)
            .sign(Algorithm.HMAC512(SECRET))
    }
}
