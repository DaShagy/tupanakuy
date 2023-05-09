package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ClassroomScreenViewModel @Inject constructor(

): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State) {
        _state.value = newState
    }

    fun updateScreenData(uid: String) {
        updateState(State.Idle(uid))
    }

    sealed class State {
        object Loading: State()
        data class Idle(val classroom: String): State()
    }
}
