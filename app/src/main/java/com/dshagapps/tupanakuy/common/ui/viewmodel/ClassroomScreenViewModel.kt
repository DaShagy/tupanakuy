package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.common.domain.model.enum.UserType
import com.dshagapps.tupanakuy.common.domain.use_case.GetClassroomByIdUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.GetClassroomUsersInfoUseCase
import com.dshagapps.tupanakuy.common.util.OperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ClassroomScreenViewModel @Inject constructor(
    private val getClassroomByIdUseCase: GetClassroomByIdUseCase,
    private val getClassroomUsersInfoUseCase: GetClassroomUsersInfoUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State) {
        _state.value = newState
    }

    fun getClassroomByIdUseCase(currentUserUID: String, classroomUid: String) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            getClassroomByIdUseCase(classroomUid) { classroomResult ->
                when (classroomResult) {
                    is OperationResult.Failure -> updateState(State.OnError(classroomResult.exception))
                    is OperationResult.Success -> {
                        getClassroomUsersInfoUseCase(
                            listOf(classroomResult.data.teacherUID) + classroomResult.data.studentUIDs
                        ) { usersResult ->
                            when (usersResult) {
                                is OperationResult.Failure -> updateState(State.OnError(usersResult.exception))
                                is OperationResult.Success -> updateState(
                                    State.Idle(
                                        currentUserUID,
                                        classroomResult.data,
                                        usersResult.data.first { user -> user.userType == UserType.TEACHER },
                                        usersResult.data.filterNot { user -> user.userType == UserType.TEACHER }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Idle(
            val currentUserUID: String,
            val classroom: Classroom,
            val teacher: User,
            val students: List<User>
        ): State()
        data class OnError(val exception: Exception): State()
    }

}