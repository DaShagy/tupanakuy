package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.use_case.GetClassroomsUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.GetUserInfoUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetClassroomUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetUserInfoUseCase
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
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase,
    private val setClassroomUseCase: SetClassroomUseCase,
    private val getClassroomsUseCase: GetClassroomsUseCase
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
                    is OperationResult.Success -> { getClassrooms(result.data) }
                }
            }
        }
    }

    fun updateUserInfo(user: User) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            setUserInfoUseCase(user) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateState(State.Idle(result.data))
                }
            }
        }
    }

    fun createClassroom(classroom: Classroom, user: User) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            setClassroomUseCase(classroom) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateState(State.Idle(user))
                }
            }
        }
    }

    fun getClassrooms(user: User) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            getClassroomsUseCase { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateState(State.Idle(user, result.data))
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
        data class Idle(
            val user: User,
            val classrooms: List<Classroom> = listOf()
        ): State()
        object OnSignOut: State()
        data class OnError(val exception: Exception): State()
    }
}