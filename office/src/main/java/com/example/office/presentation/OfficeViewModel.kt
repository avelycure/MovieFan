package com.example.office.presentation

import androidx.lifecycle.ViewModel
import com.example.office.domain.interactors.Login
import com.avelycure.authorization.domain.state.OfficeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class OfficeViewModel : ViewModel() {
    val _state = MutableStateFlow(OfficeState())
    val state = _state.asStateFlow()

}