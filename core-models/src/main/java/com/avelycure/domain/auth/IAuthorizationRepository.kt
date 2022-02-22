package com.avelycure.domain.auth

interface IAuthorizationRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(email: String, password: String): User
    suspend fun delete(email: String, password: String)
    suspend fun logOut()
    fun isUserLogined(): Boolean
    fun isEmailVerified(): Boolean
}