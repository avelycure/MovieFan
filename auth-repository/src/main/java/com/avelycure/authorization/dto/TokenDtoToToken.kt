package com.avelycure.authorization.dto

import com.avelycure.domain.models.Token

fun TokenDto.toToken(): Token {
    return Token(
        success,
        expires_at,
        request_token
    )
}