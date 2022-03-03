package com.avelycure.movie_info.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.VideoInfo
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import com.avelycure.movie_info.domain.interactors.GetMovieInfo
import com.avelycure.movie_info.domain.interactors.GetTrailerCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieInfoViewModel
@Inject constructor(
    private val getMovieInfo: GetMovieInfo,
    private val getTrailerCode: GetTrailerCode
) : ViewModel() {
    private val _state = MutableStateFlow(MovieInfoState())
    val state = _state.asStateFlow()

    //todo add shared flow for snack bar etc

    private fun getDetails(id: Int) {
        viewModelScope.launch {
            getMovieInfo.execute(id)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            _state.value = _state.value.copy(
                                movieInfo = dataState.data ?: _state.value.movieInfo,
                                images = dataState.data?.imagesBackdrop ?: emptyList(),
                                similar = dataState.data?.similar ?: emptyList()
                            )
                        }
                        is DataState.Loading -> {
                            _state.value =
                                _state.value.copy(progressBarState = dataState.progressBarState)
                        }
                        is DataState.Response -> {
                        }
                    }
                }
        }
    }

    fun onTrigger(event: MovieInfoEvents) {
        when (event) {
            is MovieInfoEvents.OnRemoveHeadFromQueue -> removeHeadMessage()
            is MovieInfoEvents.OnOpenInfoFragment -> {
                getDetails(event.movieId)
                getTrailerCode(event.movieId)
            }
            is MovieInfoEvents.VideoIsUploaded -> videoUploaded()
        }
    }

    private fun videoUploaded() {
        _state.value = _state.value.copy(
            videoIsUploaded = true
        )
    }

    private fun getTrailerCode(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getTrailerCode
                .execute(id)
                .collectLatest { dataState ->
                    when (dataState) {
                        is DataState.Data ->{
                            _state.value =
                                _state.value.copy(
                                    videoInfo = dataState.data ?: VideoInfo(),
                                    videoIsAvailable = true
                                )
                            Log.d("mytag", "Got video: " + dataState.data)
                        }
                        is DataState.Loading -> {
                            _state.value = _state.value.copy(
                                progressBarState = dataState.progressBarState
                            )
                        }
                        is DataState.Response -> {
                            appendToMessageQueue(
                                dataState.uiComponent as UIComponent.Dialog
                            )
                        }
                    }
                }
        }
    }

    private fun appendToMessageQueue(uiComponent: UIComponent) {
        val queue: Queue<UIComponent> = Queue(mutableListOf())
        for (i in 0 until _state.value.errorQueue.count())
            _state.value.errorQueue.poll()?.let { queue.add(it) }
        queue.add(uiComponent)
        _state.value = _state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = _state.value.errorQueue
            queue.remove()
            _state.value = _state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            Log.d("mytag", "Nothing to remove from MessageQueue")
        }
    }
}