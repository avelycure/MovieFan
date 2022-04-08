package com.avelycure.person.presentation

import com.avelycure.domain.models.Person
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent

data class PersonState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
    val persons: List<Person> = emptyList(),
    val lastExpandedItem: Int = -1,
    val mode: String = "popular"
)