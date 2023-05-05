package com.dshagapps.tupanakuy.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dshagapps.tupanakuy.auth.domain.model.User
import com.dshagapps.tupanakuy.auth.domain.use_case.CheckAuthStateUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignInUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignUpUseCase
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
class MainScreenViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase,
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> get() = _state

    fun updateState(newState: State){
        _state.value = newState
    }

    fun checkAuthState() = viewModelScope.launch {
        updateState(State.Loading)
        when (val result = withContext(Dispatchers.IO) { checkAuthStateUseCase() }){
            is OperationResult.Failure -> updateState(State.OnError(result.exception))
            is OperationResult.Success -> updateState(State.OnSuccess(result.data))
        }
    }

    fun signUp(email:String, password:String) =
        viewModelScope.launch {
            updateState(State.Loading)
            withContext(Dispatchers.IO) {
                signUpUseCase(email, password){ result ->
                    when (result) {
                        is OperationResult.Failure -> updateState(State.OnError(result.exception))
                        is OperationResult.Success -> updateState(State.OnSuccess(result.data))
                    }
                }
            }
        }

    fun signIn(email:String, password:String) =
        viewModelScope.launch {
            updateState(State.Loading)
            withContext(Dispatchers.IO) {
                signInUseCase(email, password){ result ->
                    when (result) {
                        is OperationResult.Failure -> updateState(State.OnError(result.exception))
                        is OperationResult.Success -> updateState(State.OnSuccess(result.data))
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
        data class OnSuccess(val user: User): State()
        data class OnError(val exception: Exception): State()
        object OnSignOut: State()
        data class OnSignUpButtonClick(
            val email: String,
            val password: String
        ): State()
        data class OnSignInButtonClick(
            val email: String,
            val password: String
        ): State()
        object OnSignOutButtonClick: State()
        class DrawAuthForm private constructor(
            val emailFieldState: TextFieldState = TextFieldState(),
            val passwordFieldState: TextFieldState = TextFieldState(),
            val formButtonState: ButtonState = ButtonState(),
            val screenButtonStates: Array<ButtonState> = arrayOf(ButtonState()),
            val user: User? = null
        ): State() {
            companion object {
                fun loginForm(
                    updateState: (State) -> Unit,
                    user: User? = null
                ): DrawAuthForm {
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
                                label = "Sign out",
                                onClick = { updateState(OnSignOut) },
                                enabled = !user?.uid.isNullOrEmpty()
                            ),
                            ButtonState(
                                label = "Change to Sign Up",
                                onClick = {
                                    updateState(signUpForm(updateState, user))
                                }
                            ),
                        ),
                        user = user
                    )
                }

                fun signUpForm(
                    updateState: (State) -> Unit,
                    user: User? = null
                ): DrawAuthForm {
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
                                label = "Sign out",
                                onClick = { updateState(OnSignOut) },
                                enabled = !user?.uid.isNullOrEmpty()
                            ),
                            ButtonState(
                                label = "Change to Login",
                                onClick = { updateState(loginForm(updateState, user)) }
                            ),
                        ),
                        user = user
                    )
                }
            }
        }
    }
}