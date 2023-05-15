package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.ui.component.form.UserForm
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.AuthScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.AuthScreenViewModelType
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AuthScreen(
    type: AuthScreenViewModelType,
    state: StateFlow<AuthScreenViewModel.State>,
    updateState: (AuthScreenViewModel.State) -> Unit = {},
    onFormButtonClick: (AuthScreenViewModel.State.AuthForm) -> Unit = {},
    onChangeScreenButtonClick: () -> Unit = {},
    goToMainScreen: (String) -> Unit = {}
) {
    val context: Context = LocalContext.current

    val authFormState = AuthScreenViewModel.State.AuthForm(
        type = type,
        onFormButtonClick = { prevState -> updateState(AuthScreenViewModel.State.OnFormButtonClick(prevState)) },
        onChangeScreenButtonClick = { updateState(AuthScreenViewModel.State.OnChangeScreenButtonClick) }
    )

    OnLifecycleEvent { _, event ->
        when (event){
            Lifecycle.Event.ON_RESUME -> updateState(authFormState)
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        AuthScreenViewModel.State.Loading -> {
            Loader()
        }
        is AuthScreenViewModel.State.OnError -> {
            AuthForm(state = s.prevState)
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
        }
        is AuthScreenViewModel.State.OnSuccess -> {
            LaunchedEffect(Unit) {
                goToMainScreen(s.user.uid)
            }
        }
        is AuthScreenViewModel.State.OnFormButtonClick -> {
            onFormButtonClick(s.prevState)
        }
        is AuthScreenViewModel.State.OnChangeScreenButtonClick -> {
            LaunchedEffect(Unit) {
                onChangeScreenButtonClick()
            }
        }
        is AuthScreenViewModel.State.AuthForm -> {
            AuthForm(state = s)
        }
    }
}

@Composable
fun AuthForm(state: AuthScreenViewModel.State.AuthForm) {
    BaseScreen(
        title = stringResource(id = R.string.app_name),
        buttonStates = arrayOf(state.changeScreenButtonState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.67f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            UserForm(
                emailFieldState = state.emailFieldState,
                passwordFieldState = state.passwordFieldState,
                buttonState = state.formButtonState
            )
        }
    }
}