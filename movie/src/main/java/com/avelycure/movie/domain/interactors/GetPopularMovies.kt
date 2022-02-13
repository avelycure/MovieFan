package com.avelycure.movie.domain.interactors

import com.avelycure.domain.models.Movie
import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularMovies(
    private val repository: IMovieRepository
) {

    fun execute(nextPage: Int): Flow<DataState<List<Movie>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            emit(DataState.Data(repository.getPopularMovies(nextPage)))
        } catch (e: Exception) {
            emit(
                DataState.Response<List<Movie>>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message ?: "1Unknown error occured + ${e.stackTrace} + ${e.cause}"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}