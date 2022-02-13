package com.avelycure.movie.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.Movie
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.UIComponent
import com.avelycure.movie.domain.interactors.GetPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val getPopularMovies: GetPopularMovies
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun fetchPopularMovies(nextPage: Int) {
        Log.d("mytag", "I got data1")
        viewModelScope.launch {
            getPopularMovies
                .execute(nextPage)
                .collect { dataState ->
                    when (dataState) {
                        is DataState.Data -> {
                            Log.d("mytag", "I got data2" + dataState.data?.toString())
                            val movies = dataState.data?.toList() ?: listOf(Movie("NONONO", "","",
                                emptyList(),10f,10f,"",1,1))
                            _state.value = _state.value.copy(movies = dataState.data ?: emptyList())
                        }
                        is DataState.Response -> {
                            Log.d(
                                "mytag",
                                "I got response" + (dataState.uiComponent as UIComponent.Dialog).description
                            )

                        }
                        is DataState.Loading -> {
                            Log.d("mytag", "I got loading")
                        }
                    }
                }

        }
    }

}