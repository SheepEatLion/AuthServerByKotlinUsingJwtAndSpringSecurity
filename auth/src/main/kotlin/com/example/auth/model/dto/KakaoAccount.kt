package com.example.auth.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoAccount(
    @JsonProperty("has_email")
    val hasEmail: Boolean,

    @JsonProperty("email_needs_agreement")
    val emailNeedsAgreement: Boolean,

    @JsonProperty("is_email_valid")
    val isEmailValid: Boolean,

    @JsonProperty("is_email_verified")
    val isEmailVerified: Boolean,

    @JsonProperty("email")
    val email: String,
)
