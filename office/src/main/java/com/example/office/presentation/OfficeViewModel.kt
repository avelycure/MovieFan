package com.example.office.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.Token
import com.example.office.domain.interactors.Login
import com.example.office.domain.state.OfficeState
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficeViewModel
@Inject constructor(private val login: Login) : ViewModel() {
    private val _state = MutableStateFlow(OfficeState())
    val state = _state.asStateFlow()


    fun onTrigger(event: OfficeEvents) {
        when (event) {
            is OfficeEvents.OnRemoveHeadFromQueue -> removeHeadMessage()
            is OfficeEvents.OnOpenInfoFragment -> {

            }
        }
    }

    fun login(){
        viewModelScope.launch {
            login.execute().collect { dataState ->
                when(dataState){
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            token = dataState.data?: Token(false,"","")
                        )
                        Log.d("mytag", "data")
                    }
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(progressBarState = dataState.progressBarState)
                        Log.d("mytag", "loading")
                    }
                    is DataState.Response -> {
                        Log.d("mytag", "resp")
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