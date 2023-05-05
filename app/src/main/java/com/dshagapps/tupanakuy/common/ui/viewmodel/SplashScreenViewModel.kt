package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Start)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    sealed class State {
        object Start: State()
        object GoToMainScreen: State()
    }
}