package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ProfileScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    state: StateFlow<ProfileScreenViewModel.State>,
    updateState: (ProfileScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    goToPreviousScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        is ProfileScreenViewModel.State.Idle -> {
            Text("${s.user}")
        }
        ProfileScreenViewModel.State.Loading -> Loader()
        is ProfileScreenViewModel.State.onError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                goToPreviousScreen()
            }
        }
    }
}