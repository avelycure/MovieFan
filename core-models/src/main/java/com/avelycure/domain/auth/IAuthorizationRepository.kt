package com.avelycure.domain.auth

interface IAuthorizationRepository {
    suspend fun login()
}