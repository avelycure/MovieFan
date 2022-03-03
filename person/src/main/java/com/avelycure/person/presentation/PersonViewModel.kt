package com.avelycure.person.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import com.avelycure.person.domain.interactors.GetPopularPersons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel
@Inject constructor(
    private val getPopularPersons: GetPopularPersons
) : ViewModel() {

    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()

    fun onTrigger(event: PersonEvents) {
        when (event) {
            is PersonEvents.OnRemoveHeadFromQueue -> removeHeadMessage()
            is PersonEvents.OnOpenPersonScreen -> {
                getPopularPerson()
            }
        }
    }

    private fun getPopularPerson() {
        viewModelScope.launch {
            getPopularPersons.execute(_state.value.lastVisiblePage).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        Log.d("mytag", "GOT DATA: ${dataState.data}")
                        _state.value = _state.value.copy(
                            persons = _state.value.persons + (dataState.data?: emptyList()),
                            lastVisiblePage = _state.value.lastVisiblePage + 1
                        )
                    }
                    is DataState.Loading -> {
                        Log.d("mytag", "GOT LOAD")
                        _state.value = _state.value.copy(
                            progressBarState = dataState.progressBarState
                        )
                    }
                    is DataState.Response -> {
                        Log.d("mytag", "GOT PROB")
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