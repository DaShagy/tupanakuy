package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.use_case.GetUserInfoUseCase
import com.dshagapps.tupanakuy.common.util.OperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State) {
        _state.value = newState
    }

    fun getUserInfo(uid: String) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            getUserInfoUseCase(uid) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateState(State.Idle(result.data))
                }
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        updateState(State.Loading)
        when (withContext(Dispatchers.IO) { signOutUseCase() }){
            else -> updateState(State.OnSignOut)
        }
    }

    sealed class State {
        object Loading: State()
        data class Idle(val user: User): State()
        object OnSignOut: State()
        data class OnError(val exception: Exception): State()
    }
}