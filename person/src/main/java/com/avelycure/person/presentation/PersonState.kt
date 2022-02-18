package com.avelycure.person.presentation

import com.avelycure.domain.models.Person
import com.avelycure.domain.state.ProgressBarState

data class PersonState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val persons: List<Person> = emptyList(),
    val lastVisiblePage: Int = 1
)