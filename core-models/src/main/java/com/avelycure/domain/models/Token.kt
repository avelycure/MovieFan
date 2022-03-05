package com.avelycure.domain.models

data class Token(
    val success: Boolean,
    val expiresAt: String,
    val requestToken: String
)