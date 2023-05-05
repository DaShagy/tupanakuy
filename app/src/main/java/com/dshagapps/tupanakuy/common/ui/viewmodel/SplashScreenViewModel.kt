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

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Start)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    fun checkAuthState() = viewModelScope.launch {
        when (val result = withContext(Dispatchers.IO) { checkAuthStateUseCase() }) {
            is OperationResult.Failure -> updateState(State.GoToAuthScreen)
            is OperationResult.Success -> updateState(State.GoToMainScreen(result.data.uid))
        }
    }

    sealed class State {
        object Start: State()
        object GoToAuthScreen: State()
        data class GoToMainScreen(val uid: String): State()
    }
}