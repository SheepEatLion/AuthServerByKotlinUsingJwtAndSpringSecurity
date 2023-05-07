package com.example.auth.model.dto

import org.jetbrains.annotations.NotNull

data class GenerateTokenRequest(
    @field:NotNull
    val id: Long,

    @field:NotNull
    val email: String,
)