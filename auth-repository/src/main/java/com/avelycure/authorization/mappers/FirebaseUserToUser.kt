package com.avelycure.authorization.mappers

import com.avelycure.domain.auth.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User {
    return User(
        name = displayName ?: "",
        email = email ?: ""
    )
}