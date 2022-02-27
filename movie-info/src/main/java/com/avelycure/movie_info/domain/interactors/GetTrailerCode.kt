package com.avelycure.movie_info.domain.interactors

import com.avelycure.domain.constants.ErrorCodes
import com.avelycure.domain.constants.ErrorCodes.ERROR_NO_TRAILER_CODE
import com.avelycure.domain.models.VideoInfo
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.UIComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Makes request to server and returns trailer code on youtube inside
 * VideoInfo object or -1 if there are no trailers for this movie
 */
class GetTrailerCode(
    private val repository: IMovieInfoRepository
) {
    fun execute(id: Int): Flow<DataState<VideoInfo>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))
            val result = repository.getTrailerCode(id)
            if (result.key != ERROR_NO_TRAILER_CODE)
                emit(DataState.Data(result))
            else
                emit(
                    DataState.Response<VideoInfo>(
                        uiComponent = UIComponent.Dialog(
                            description = ErrorCodes.ERROR_NO_TRAILER_AVAILABLE,
                            title = "No trailer available"
                        )
                    )
                )
        } catch (e: Exception) {
            emit(
                DataState.Response<VideoInfo>(
                    uiComponent = UIComponent.Dialog(
                        description = e.message ?: "Unknown error occurred",
                        title = "No trailer available"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }

}
