package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.common.domain.model.Classroom
import com.dshagapps.tupanakuy.common.domain.use_case.GetClassroomByIdUseCase
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
    private val getClassroomByIdUseCase: GetClassroomByIdUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State) {
        _state.value = newState
    }

    fun getClassroomByIdUseCase(classroomUid: String) = viewModelScope.launch {
        updateState(State.Loading)
        withContext(Dispatchers.IO) {
            getClassroomByIdUseCase(classroomUid) { result ->
                when (result) {
                    is OperationResult.Failure -> updateState(State.OnError(result.exception))
                    is OperationResult.Success -> updateState(State.Idle(result.data))
                }
            }
        }
    }

    sealed class State {
        object Loading: State()
        data class Idle(val classroom: Classroom): State()
        data class OnError(val exception: Exception): State()
    }
}
