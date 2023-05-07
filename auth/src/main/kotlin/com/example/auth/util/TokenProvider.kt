package com.example.auth.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
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

    fun generateRefreshToken(authentication: Authentication, email: String): String {
        return JWT.create()
            .withSubject(authentication.name)
            .withExpiresAt(Date(Date().time + refreshTokenValidityMilliseconds))
            .withClaim("email", email)
            .sign(Algorithm.HMAC512(SECRET))
    }

    fun getAuthentication(token: String): Authentication {
        val decodedJwt = toDecodedJwt(token = token)

        val authorities: Collection<GrantedAuthority> = Arrays
            .stream(decodedJwt.claims["auth"].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .map { role: String? -> SimpleGrantedAuthority(role) }
            .collect(Collectors.toList())

        val principal = User(decodedJwt.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    fun validate(token: String): Boolean {
        val decodedJwt = toDecodedJwt(token = token)
        verifyExpiresAt(decodedJwt = decodedJwt)
        verifySubject(decodedJwt = decodedJwt)
        return true
    }

    private fun toDecodedJwt(token: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token)
    }

    private fun verifyExpiresAt(decodedJwt: DecodedJWT) {
        if(decodedJwt.expiresAt == null || Date().after(decodedJwt.expiresAt)) {
            throw JWTVerificationException("expired token")
        }
    }

    private fun verifySubject(decodedJwt: DecodedJWT) {
        if(decodedJwt.subject == null) {
            throw JWTVerificationException("empty subject")
        }
    }

    fun getId(refreshToken: String): Long {
        val decodedRefreshToken = toDecodedJwt(token = refreshToken)
        return decodedRefreshToken.subject.toLong()
    }

    fun getEmail(refreshToken: String): String {
        val decodedRefreshToken = toDecodedJwt(token = refreshToken)
        return decodedRefreshToken.claims["email"].toString()
    }
}
