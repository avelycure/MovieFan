package com.avelycure.person.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avelycure.domain.models.Person
import com.avelycure.domain.models.PersonInfo
import com.avelycure.domain.state.DataState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent
import com.avelycure.person.domain.interactors.GetPersonInfo
import com.avelycure.person.domain.interactors.GetPopularPersons
import com.avelycure.person.utils.setProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val TAG = "PersonViewModel"

@HiltViewModel
class PersonViewModel
@Inject constructor(
    private val getPopularPersons: GetPopularPersons,
    private val getPersonInfo: GetPersonInfo
) : ViewModel() {

    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()

    fun onTrigger(event: PersonEvents) {
        when (event) {
            is PersonEvents.OnRemoveHeadFromQueue -> removeHeadMessage()
            is PersonEvents.OnRequestMorePersons -> {
                getPopularPerson()
            }
            is PersonEvents.OnExpandPerson -> {
                onExpand(event.personId, event.itemId)
            }
        }
    }

    private fun onExpand(personId: Int, itemId: Int) {
        viewModelScope.launch {
            getPersonInfo.execute(personId).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        val list = _state.value.persons.toMutableList()
                        val updatedPerson = list[itemId].copy()
                        updatedPerson.setProperties(dataState.data)
                        updatedPerson.expanded = !list[itemId].expanded
                        list[itemId] = updatedPerson
                        _state.value = _state.value.copy(
                            persons = list,
                            lastExpandedItem = itemId
                        )
                    }
                    is DataState.Response -> {
                        appendToMessageQueue(
                            dataState.uiComponent as UIComponent.Dialog
                        )
                    }
                    is DataState.Loading -> {
                       _state.value = _state.value.copy(
                           progressBarState = dataState.progressBarState
                        )
                    }
                }
            }
        }
    }

    private fun getPopularPerson() {
        viewModelScope.launch {
            getPopularPersons.execute(_state.value.lastVisiblePage).collect { dataState ->
                when (dataState) {
                    is DataState.Data -> {
                        _state.value = _state.value.copy(
                            persons = _state.value.persons + (dataState.data ?: emptyList()),
                            lastVisiblePage = _state.value.lastVisiblePage + 1
                        )
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
            Log.d(TAG, "Nothing to remove from MessageQueue")
        }
    }
}