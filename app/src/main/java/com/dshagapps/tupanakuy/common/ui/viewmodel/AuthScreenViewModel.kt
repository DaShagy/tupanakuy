package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.auth.domain.use_case.AuthUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetUserInfoIfNotExistsUseCase
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.TextFieldState
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.dshagapps.tupanakuy.common.util.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class AuthScreenViewModelType {
    object SignIn: AuthScreenViewModelType()
    object SignUp: AuthScreenViewModelType()
}

abstract class AuthScreenViewModel(
    private val authUseCase: AuthUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val setUserInfoIfNotExistsUseCase: SetUserInfoIfNotExistsUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    abstract fun getViewModelType(): AuthScreenViewModelType

    fun updateState(newState: State){
        _state.value = newState
    }

    fun auth(prevState: State.AuthForm) =
        viewModelScope.launch {
            updateState(State.Loading)
            withContext(Dispatchers.IO) {
                authUseCase(prevState.emailFieldState.value, prevState.passwordFieldState.value) { result ->
                    when (result) {
                        is OperationResult.Failure -> updateState(State.OnError(result.exception, prevState))
                        is OperationResult.Success -> {
                            setUserInfoIfNotExistsUseCase(result.data) { setUserInfoResult ->
                                when (setUserInfoResult) {
                                    is OperationResult.Failure -> {
                                        signOutUseCase()
                                        updateState(State.OnError(setUserInfoResult.exception, prevState))
                                    }
                                    is OperationResult.Success -> {
                                        updateState(State.OnSuccess(setUserInfoResult.data))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    sealed class State {
        object Loading: State()
        data class OnSuccess(val user: User): State()
        data class OnError(val exception: Exception, val prevState: AuthForm): State()
        data class OnFormButtonClick(val prevState: AuthForm): State()
        object OnChangeScreenButtonClick: State()
       data class AuthForm(
           private val type: AuthScreenViewModelType,
           private val onFormButtonClick: (AuthForm) -> Unit,
           private val onChangeScreenButtonClick: () -> Unit
       ): State() {
            val emailFieldState = TextFieldState(
                _validator = { !Validator.validateEmail(it) },
                label = EMAIL_LABEL
            )
            val passwordFieldState = TextFieldState(
                _validator = { !Validator.validatePassword(it) },
                label = PASSWORD_LABEL
            )
            val formButtonState: ButtonState = ButtonState(
                label = when (type) {
                    AuthScreenViewModelType.SignIn -> SIGN_IN_LABEL
                    AuthScreenViewModelType.SignUp -> SIGN_UP_LABEL
                },
                onClick = { onFormButtonClick(this) }
            )
            val changeScreenButtonState: ButtonState = ButtonState(
                label = when (type) {
                    AuthScreenViewModelType.SignIn -> CHANGE_TO_SIGN_UP_LABEL
                    AuthScreenViewModelType.SignUp -> CHANGE_TO_SIGN_IN_LABEL
                },
                onClick = { onChangeScreenButtonClick() }
            )
        }

        companion object {
            private const val PASSWORD_LABEL = "Password"
            private const val EMAIL_LABEL = "Email"
            private const val SIGN_UP_LABEL = "Sign Up"
            private const val SIGN_IN_LABEL = "Sign In"
            private const val CHANGE_TO_SIGN_IN_LABEL = "Change to sign in"
            private const val CHANGE_TO_SIGN_UP_LABEL = "Change to sign up"
        }
    }
}