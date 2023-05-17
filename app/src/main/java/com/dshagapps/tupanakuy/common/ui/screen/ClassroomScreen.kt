package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ClassroomScreen(
    state: StateFlow<ClassroomScreenViewModel.State>,
    updateState: (ClassroomScreenViewModel.State) -> Unit = {},
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
        is ClassroomScreenViewModel.State.Idle -> {
            Column {
                Text(text = "UserUID: ${s.currentUserUID}")
                Spacer(modifier = Modifier.padding(8.dp))
                Text("Classroom: ${s.classroom}")
            }
        }
        ClassroomScreenViewModel.State.Loading -> Loader()
        is ClassroomScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            goToPreviousScreen()
        }
    }
}