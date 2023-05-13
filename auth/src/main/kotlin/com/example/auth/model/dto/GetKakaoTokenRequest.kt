package com.example.auth.model.dto

data class GetKakaoTokenRequest(
    val code: String,

    val clientId: String,

    val clientSecret: String,

    val redirectUri: String,

    val grantType: String = "authorization_code",
) {
    override fun toString(): String {
        return "code=" + code + '&' +
                "client_id=" + clientId + '&' +
                "client_secret=" + clientSecret + '&' +
                "redirect_uri=" + redirectUri + '&' +
                "grant_type=" + grantType
    }
}
