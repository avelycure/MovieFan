package com.example.office.domain.interactors

import com.avelycure.domain.auth.IAuthorizationRepository
import com.avelycure.domain.models.PersonInfo
import com.avelycure.domain.models.Token
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Login(
    val authRepository: IAuthorizationRepository
) {

    fun execute() : Flow<DataState<Token>> = flow{
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))
            val result = authRepository.getToken()
            emit(DataState.Data(result))
        }catch (e: Exception){
            emit(
                DataState.Response<Token>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message ?: "Unknown error occurred",
                        title = "Some error occurred"
                    )
                )
            )
        }
        finally{
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}