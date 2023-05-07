package com.example.auth.model.dto

import org.jetbrains.annotations.NotNull

data class GenerateTokenRequest(
    @field:NotNull
    val memberId: Long,

    @field:NotNull
    val memberEmail: String,
)