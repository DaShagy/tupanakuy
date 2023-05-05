package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MainScreen(
    state: StateFlow<MainScreenViewModel.State>,
    updateState: (MainScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSignOutButtonClick: () -> Unit = {},
    goToAuthScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        is MainScreenViewModel.State.Idle -> {
            BaseScreen(
                title = stringResource(id = R.string.app_name),
                buttonStates = arrayOf(
                    ButtonState(
                        label = "Sign out",
                        onClick = onSignOutButtonClick,
                        enabled = true
                    )
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("UID: ${s.user.uid}")
                    Text("Email: ${s.user.email}")
                }
            }
        }
        MainScreenViewModel.State.Loading -> Loader()
        MainScreenViewModel.State.OnSignOut -> {
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
        is MainScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
    }
}