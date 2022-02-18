package com.avelycure.movie_info.domain.interactors

import com.avelycure.domain.models.MovieInfo
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieInfo(
    private val repository: IMovieInfoRepository,
) {

    fun execute(id: Int): Flow<DataState<MovieInfo>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            emit(DataState.Data(data = repository.getDetails(id)))
        } catch (e: Exception) {
            emit(
                DataState.Response<MovieInfo>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message
                            ?: "GetMovieInfo: error occurred + ${e.stackTrace} + ${e.cause}",
                        title = "Error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))

        }
    }

}