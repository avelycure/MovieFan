package com.avelycure.person.domain.interactors

import android.util.Log
import com.avelycure.domain.models.PersonInfo
import com.avelycure.domain.repository.IPersonRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPersonInfo(
    private val repository: IPersonRepository
) {
    fun execute(id: Int): Flow<DataState<PersonInfo>> = flow {
        try {
            Log.d("mytag", "sent request")
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))
            val result = repository.getPersonInfo(id)
            emit(DataState.Data(result))
            Log.d("mytag", "got data")
        } catch (e: Exception) {
            emit(
                DataState.Response<PersonInfo>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message ?: "Unknown error occurred",
                        title = "Some error occurred"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}