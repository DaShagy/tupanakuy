package com.dshagapps.tupanakuy.common.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    updateState: (MainScreenViewModel.State) -> Unit = {}
) {

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> updateState(MainScreenViewModel.State.Idle)
            else -> Unit
        }
    }

    when (state.collectAsState().value) {
        MainScreenViewModel.State.Idle -> {
            BaseScreen(
                title = stringResource(id = R.string.app_name)
            ) {
                Text("Main Screen")
            }
        }
        MainScreenViewModel.State.Loading -> Loader()
    }
}