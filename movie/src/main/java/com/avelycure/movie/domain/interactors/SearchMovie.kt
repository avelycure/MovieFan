package com.avelycure.movie.domain.interactors

import com.avelycure.domain.models.Movie
import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class SearchMovie(private val repository: IMovieRepository) {

    fun execute(title: String, page: Int): Flow<DataState<List<Movie>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            emit(DataState.Data(repository.searchMovie(title, page)))
        } catch (e: Exception) {
            DataState.Response<List<Movie>>(
                uiComponent = UIComponent.Dialog(
                    description = e.message
                        ?: "SearchMovie: error occurred + ${e.stackTrace} + ${e.cause}",
                    title = "Error"
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}