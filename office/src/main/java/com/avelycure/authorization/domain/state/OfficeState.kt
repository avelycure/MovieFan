package com.avelycure.authorization.domain.state

import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent

data class OfficeState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
    val userAuthenticated: Boolean = false,
)