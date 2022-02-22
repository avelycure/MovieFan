package com.avelycure.authorization.domain.interactors

import com.avelycure.domain.auth.IAuthorizationRepository
import com.avelycure.domain.auth.User
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Login(
    private val repository: IAuthorizationRepository
) {

    fun execute() {

    }
}