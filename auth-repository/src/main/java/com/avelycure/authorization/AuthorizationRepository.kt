package com.avelycure.authorization

import com.avelycure.authorization.dto.toToken
import com.avelycure.authorization.service.RequestTokenService
import com.avelycure.domain.auth.IAuthorizationRepository
import com.avelycure.domain.models.Token

internal class AuthorizationRepository(
    val requestTokenService: RequestTokenService
): IAuthorizationRepository {

    override suspend fun getToken(): Token {
        return requestTokenService.requestToken().toToken()
    }
}