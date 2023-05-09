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
    private val setClassroomUseCase: SetClassroomUseCase,
    private val getClassroomsUseCase: GetClassroomsUseCase,
    private val addStudentToClassroomUseCase: AddStudentToClassroomUseCase,
    private val removeStudentFromClassroomUseCase: RemoveStudentFromClassroomUseCase
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
                    is OperationResult.Failure -> updateState(State.OnAuthError(userResult.exception))
                    is OperationResult.Success -> {
                        getClassroomsUseCase { classroomResult ->
                            when (classroomResult) {
                                is OperationResult.Failure -> {
                                    updateState(
                                        State.OnError(
                                            classroomResult.exception,
                                            State.Idle(userResult.data)
                                        )
                                    )
                                }
                                is OperationResult.Success -> updateState(State.Idle(userResult.data, classroomResult.data))
                            }
                        }
                    }
                }
            }
        }
    }

    fun createClassroom(prevState: State.Idle) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            setClassroomUseCase(Classroom(teacherUID = prevState.user.uid)) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception, prevState))
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
                    is OperationResult.Failure -> updateState(State.OnError(result.exception, prevState))
                    is OperationResult.Success -> updateScreenData(prevState.user.uid)
                }
            }
        }
    }

    fun classroomSignOut(classroomUid: String, prevState: State.Idle) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            removeStudentFromClassroomUseCase(classroomUid, prevState.user.uid) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception, prevState))
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
        data class OnClassroomSignOut(val classroomUid: String, val prevState: Idle) : State()
        data class OnAuthError(val exception: Exception): State()
        data class OnError(val exception: Exception, val prevState: Idle): State()
        data class GoToClassroom(val classroomUid: String) : State()
    }
}