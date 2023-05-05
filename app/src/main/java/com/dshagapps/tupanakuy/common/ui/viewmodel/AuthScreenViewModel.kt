package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.common.domain.model.User
import com.dshagapps.tupanakuy.auth.domain.use_case.SignInUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignUpUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.CheckUserInfoUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetUserInfoUseCase
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.TextFieldState
import com.dshagapps.tupanakuy.common.util.OperationResult
import com.dshagapps.tupanakuy.common.util.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val checkUserInfoUseCase: CheckUserInfoUseCase,
    private val setUserInfoUseCase: SetUserInfoUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    fun signUp(email:String, password:String) =
        viewModelScope.launch {
            updateState(State.Loading)
            withContext(Dispatchers.IO) {
                signUpUseCase(email, password) { result ->
                    when (result) {
                        is OperationResult.Failure -> updateState(State.OnError(result.exception))
                        is OperationResult.Success -> {
                            checkUserInfoUseCase(result.data.uid) { checkUserInfoResult ->
                                when (checkUserInfoResult) {
                                    is OperationResult.Failure -> {
                                        setUserInfoUseCase(result.data) { setUserInfoResult ->
                                            when (setUserInfoResult) {
                                                is OperationResult.Failure -> {
                                                    signOutUseCase()
                                                    updateState(State.OnError(setUserInfoResult.exception))
                                                }
                                                is OperationResult.Success -> {
                                                    updateState(State.OnSuccess(setUserInfoResult.data))
                                                }
                                            }
                                        }
                                    }
                                    is OperationResult.Success -> {
                                        updateState(State.OnSuccess(result.data))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    fun signIn(email:String, password:String) =
        viewModelScope.launch {
            updateState(State.Loading)
            withContext(Dispatchers.IO) {
                signInUseCase(email, password) { result ->
                    when (result) {
                        is OperationResult.Failure -> updateState(State.OnError(result.exception))
                        is OperationResult.Success -> {
                            checkUserInfoUseCase(result.data.uid) { checkUserInfoResult ->
                                when (checkUserInfoResult) {
                                    is OperationResult.Failure -> {
                                        setUserInfoUseCase(result.data) { setUserInfoResult ->
                                            when (setUserInfoResult) {
                                                is OperationResult.Failure -> {
                                                    signOutUseCase()
                                                    updateState(State.OnError(setUserInfoResult.exception))
                                                }
                                                is OperationResult.Success -> {
                                                    updateState(State.OnSuccess(setUserInfoResult.data))
                                                }
                                            }
                                        }
                                    }
                                    is OperationResult.Success -> {
                                        updateState(State.OnSuccess(result.data))
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
        data class OnError(val exception: Exception): State()
        data class OnSignUpButtonClick(
            val email: String,
            val password: String
        ): State()
        data class OnSignInButtonClick(
            val email: String,
            val password: String
        ): State()
        class DrawAuthForm private constructor(
            val emailFieldState: TextFieldState = TextFieldState(),
            val passwordFieldState: TextFieldState = TextFieldState(),
            val formButtonState: ButtonState = ButtonState(),
            val screenButtonStates: Array<ButtonState> = arrayOf(ButtonState())
        ): State() {
            companion object {
                fun loginForm(updateState: (State) -> Unit): DrawAuthForm {
                    val emailFieldState = TextFieldState(
                        _validator = { !Validator.validateEmail(it) },
                        label = "Email"
                    )
                    val passwordFieldState = TextFieldState(
                        _validator = { !Validator.validatePassword(it) },
                        label = "Password"
                    )

                    return DrawAuthForm(
                        emailFieldState = emailFieldState,
                        passwordFieldState = passwordFieldState,
                        formButtonState = ButtonState(
                            label = "Login",
                            onClick = {
                                updateState(OnSignInButtonClick(
                                    emailFieldState.value, passwordFieldState.value)
                                )
                            }
                        ),
                        screenButtonStates = arrayOf(
                            ButtonState(
                                label = "Change to Sign Up",
                                onClick = {
                                    updateState(signUpForm(updateState))
                                }
                            ),
                        )
                    )
                }

                fun signUpForm(updateState: (State) -> Unit): DrawAuthForm {
                    val emailFieldState = TextFieldState(
                        _validator = { !Validator.validateEmail(it) },
                        label = "Email"
                    )
                    val passwordFieldState = TextFieldState(
                        _validator = { !Validator.validatePassword(it) },
                        label = "Password"
                    )

                    return DrawAuthForm(
                        emailFieldState = emailFieldState,
                        passwordFieldState = passwordFieldState,
                        formButtonState = ButtonState(
                            label = "Sign up",
                            onClick = {
                                updateState(OnSignUpButtonClick(
                                    emailFieldState.value, passwordFieldState.value)
                                )
                            }
                        ),
                        screenButtonStates = arrayOf(
                            ButtonState(
                                label = "Change to Login",
                                onClick = { updateState(loginForm(updateState)) }
                            ),
                        )
                    )
                }
            }
        }
    }
}