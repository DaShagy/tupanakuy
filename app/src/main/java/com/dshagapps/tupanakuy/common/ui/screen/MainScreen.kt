package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
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
                Text(text = "Hello Android!")
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