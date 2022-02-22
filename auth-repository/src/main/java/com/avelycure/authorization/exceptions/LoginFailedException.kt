package com.avelycure.authorization.exceptions

class LoginFailedException(): Exception() {
    override val message: String
        get() = "Something went wrong while log in"
}