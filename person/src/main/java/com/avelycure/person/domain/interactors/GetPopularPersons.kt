package com.avelycure.person.domain.interactors

import com.avelycure.domain.models.Person
import com.avelycure.domain.repository.IPersonRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetPopularPersons(
    private val repository: IPersonRepository
) {

    fun execute(page: Int): Flow<DataState<List<Person>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val result = repository.getPopularPersons(page)

            emit(DataState.Data(result))

        } catch (e: Exception) {
            emit(
                DataState.Response<List<Person>>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message
                            ?: "GetPopularPerson: error occurred + ${e.stackTrace} + ${e.cause}",
                        title = "Error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}