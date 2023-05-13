package com.example.auth.service

import com.example.auth.model.dto.*
import com.example.auth.util.KakaoTokenFeignClient
import com.example.auth.util.KakaoUserFeignClient
import com.example.auth.util.TokenProvider
import org.springframework.beans.factory.annotation.Value
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
    val kakaoTokenFeignClient: KakaoTokenFeignClient,
    val kakaoUserFeignClient: KakaoUserFeignClient,

    @Value("\${oauth2.kakao.clientId}")
    val clientId: String,

    @Value("\${oauth2.kakao.secretKey")
    val clientSecret: String,
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

    fun getKakaoUser(getKakaoUserEmailRequest: GetKakaoUserEmailRequest): GetKakaoUserEmailResponse {
        val kakaoTokenResponse: GetKakaoTokenResponse = kakaoTokenFeignClient.getToken(
            GetKakaoTokenRequest(
                code = getKakaoUserEmailRequest.code,
                redirectUri = getKakaoUserEmailRequest.redirectUri,
                clientSecret = clientSecret,
                clientId = clientId,
            ).toString()
        )
        val token: String = "Bearer ${kakaoTokenResponse.accessToken}"
        val kakaoUserResponse: GetKakaoUserResponse = kakaoUserFeignClient.getUser(token)

        return GetKakaoUserEmailResponse(kakaoUserResponse.kakaoAccount.email)
    }
}
