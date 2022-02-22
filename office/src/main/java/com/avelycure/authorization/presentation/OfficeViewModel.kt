package com.avelycure.authorization.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.avelycure.authorization.domain.interactors.Login
import com.avelycure.authorization.domain.state.OfficeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OfficeViewModel
@Inject constructor(
    private val login: Login,
) : ViewModel() {
    val state: MutableState<OfficeState> = mutableStateOf(OfficeState())


}