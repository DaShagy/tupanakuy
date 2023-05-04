package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dshagapps.tupanakuy.common.ui.util.FieldState
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(
    state: StateFlow<MainScreenViewModel.State>,
    updateState: (MainScreenViewModel.State) -> Unit = {},
    onScreenResume: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event){
            Lifecycle.Event.ON_RESUME -> onScreenResume()
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        MainScreenViewModel.State.Idle -> {
            BaseScreen(title = stringResource(id = R.string.app_name) ) {
                Box(
                    modifier = Modifier.fillMaxWidth(0.67f).fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    UserForm(
                        emailFieldState = FieldState(label = "email"),
                        passwordFieldState = FieldState(label = "password"),
                        onButtonClick = {},
                        buttonLabel = "Sign In"
                    )
                }
            }
        }
        MainScreenViewModel.State.Loading -> {
            Loader()
        }
        is MainScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            updateState(MainScreenViewModel.State.Idle)
        }
        is MainScreenViewModel.State.OnSuccess -> {
            Toast.makeText(context, s.user.uid, Toast.LENGTH_SHORT).show()
            updateState(MainScreenViewModel.State.Idle)
        }
    }
}