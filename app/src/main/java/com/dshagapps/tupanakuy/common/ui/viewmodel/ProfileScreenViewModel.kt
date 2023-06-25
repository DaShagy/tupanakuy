package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ProfileScreenViewModel @Inject constructor(
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
            getUserInfoUseCase(uid) { userResult ->
                when (userResult) {
                    is OperationResult.Failure -> updateState(State.onError(userResult.exception))
                    is OperationResult.Success -> updateState(State.Idle(userResult.data))
                }
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Idle(val user: User): State()
        data class onError(val exception: Exception): State()
    }
}