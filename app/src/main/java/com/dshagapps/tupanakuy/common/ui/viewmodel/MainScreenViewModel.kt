package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenViewModel private constructor(): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    sealed class State {
        object Loading: State()
        object Idle: State()
    }

    companion object {
        @Volatile
        private var INSTANCE: MainScreenViewModel? = null

        fun getInstance(): MainScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MainScreenViewModel().also { INSTANCE = it }
            }
        }
    }
}