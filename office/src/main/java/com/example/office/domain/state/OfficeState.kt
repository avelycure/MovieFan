package com.example.office.domain.state

import android.media.session.MediaSession
import com.avelycure.domain.models.Token
import com.avelycure.domain.state.ProgressBarState
import com.avelycure.domain.state.Queue
import com.avelycure.domain.state.UIComponent

data class OfficeState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
    val token: Token = Token(false, "", ""),
)