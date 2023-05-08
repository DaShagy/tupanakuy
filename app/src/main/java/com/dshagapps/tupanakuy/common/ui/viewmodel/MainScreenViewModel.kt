package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.use_case.*
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
    private val getClassroomsUseCase: GetClassroomsUseCase,
    private val addStudentToClassroomUseCase: AddStudentToClassroomUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State) {
        _state.value = newState
    }

    fun updateScreenData(uid: String) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            getUserInfoUseCase(uid) { userResult ->
                when (userResult) {
                    is OperationResult.Failure -> updateState(State.OnError(userResult.exception))
                    is OperationResult.Success -> {
                        getClassroomsUseCase { classroomResult ->
                            when (classroomResult) {
                                is OperationResult.Failure -> updateState(State.OnError(classroomResult.exception))
                                is OperationResult.Success -> updateState(State.Idle(userResult.data, classroomResult.data))
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateUserInfo(prevState: State.Idle) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            setUserInfoUseCase(prevState.user) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateScreenData(prevState.user.uid)
                }
            }
        }
    }

    fun createClassroom(prevState: State.Idle) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            setClassroomUseCase(Classroom()) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateScreenData(prevState.user.uid)
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

    fun classroomSignUp(classroomUid: String, prevState: State.Idle) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            addStudentToClassroomUseCase(classroomUid, prevState.user.uid) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateScreenData(prevState.user.uid)
                }
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Idle(
            val user: User = User(),
            val classrooms: List<Classroom> = listOf()
        ): State()
        object OnSignOut: State()
        data class OnCreateClassroom(val prevState: Idle): State()
        data class OnClassroomSignUp(val classroomUid: String, val prevState: Idle) : State()
        data class OnError(val exception: Exception): State()
    }
}