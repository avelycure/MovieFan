package com.avelycure.domain.auth

import com.avelycure.domain.models.Token

interface IAuthorizationRepository {
    suspend fun getToken():Token
}