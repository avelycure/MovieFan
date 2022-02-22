package com.avelycure.authorization

import com.avelycure.authorization.exceptions.LoginFailedException
import com.avelycure.authorization.mappers.toUser
import com.avelycure.domain.auth.IAuthorizationRepository
import com.avelycure.domain.auth.User
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class AuthorizationRepository : IAuthorizationRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override suspend fun login(email: String, password: String): User =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = auth.currentUser?.toUser() ?: User("", "")
                    continuation.resume(user)
                }
                .addOnFailureListener {
                    val exception = LoginFailedException()
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun register(email: String, password: String): User =
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = auth.currentUser?.toUser() ?: User("", "")
                    continuation.resume(user)
                }
                .addOnFailureListener {
                    val exception = LoginFailedException()
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun delete(email: String, password: String) {

    }

    override suspend fun logOut() {
        auth.signOut()
    }

    override fun isUserLogined(): Boolean = auth.currentUser != null
    override fun isEmailVerified(): Boolean = auth.currentUser?.isEmailVerified ?: false
}