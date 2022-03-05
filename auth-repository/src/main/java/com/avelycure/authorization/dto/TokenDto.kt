package com.avelycure.authorization.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)