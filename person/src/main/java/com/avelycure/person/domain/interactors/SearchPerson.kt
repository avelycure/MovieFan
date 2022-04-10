package com.avelycure.person.domain.interactors

import com.avelycure.domain.models.Person
import com.avelycure.domain.repository.IPersonRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchPerson(
    private val repository: IPersonRepository
) {

    fun execute(query: String, page: Int): Flow<DataState<List<Person>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            emit(DataState.Data(repository.searchPerson(query, page)))
        } catch (e: Exception) {
            emit(
                DataState.Response<List<Person>>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message
                            ?: "Search movie: error occurred + ${e.stackTrace} + ${e.cause}",
                        title = "Error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}