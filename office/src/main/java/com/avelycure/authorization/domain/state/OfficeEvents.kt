package com.avelycure.authorization.domain.state

import com.avelycure.domain.state.UIComponent

sealed class OfficeEvents {
    //common
    data class Error(
        val uiComponent: UIComponent
    ) : OfficeEvents()

    object OnRemoveHeadFromQueue : OfficeEvents()

    //login
    data class UserClickedLogin(
        val email: String,
        val password: String
    ) : OfficeEvents()

    //register
    data class UserClickedRegister(
        val email: String,
        val password: String,
        val passwordRepeat: String
    ) : OfficeEvents()

    object UserEnteredLoginScreen : OfficeEvents()
}