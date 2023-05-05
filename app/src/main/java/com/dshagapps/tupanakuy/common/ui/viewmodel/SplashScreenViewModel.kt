package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.auth.domain.use_case.CheckAuthStateUseCase
import com.dshagapps.tupanakuy.common.util.OperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Start { checkAuthState() })
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    fun checkAuthState() = viewModelScope.launch {
        when (withContext(Dispatchers.IO) { checkAuthStateUseCase() }) {
            is OperationResult.Failure -> updateState(State.GoToAuthScreen)
            is OperationResult.Success -> updateState(State.GoToMainScreen)
        }
    }

    sealed class State {
        data class Start(val checkAuthState: () -> Unit = {}): State()
        object GoToAuthScreen: State()
        object GoToMainScreen: State()
    }
}