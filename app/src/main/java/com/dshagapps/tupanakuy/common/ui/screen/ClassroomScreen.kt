package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.component.screen.ChatScreen
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomScreen(
    state: StateFlow<ClassroomScreenViewModel.State>,
    updateState: (ClassroomScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSendButtonClick: (String, String, ClassroomScreenViewModel.State.Idle) -> Unit = { _, _, _ -> },
    goToPreviousScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            Lifecycle.Event.ON_PAUSE -> updateState(ClassroomScreenViewModel.State.Loading)
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        is ClassroomScreenViewModel.State.Idle -> {
            BaseScreen(title = "Classroom: ${s.classroom.uid}, Chat: ${s.classroom.chatUID}") {
                ChatScreen(
                    onSendButtonClick = { message ->
                        onSendButtonClick(message, s.classroom.teacherUID, s)
                    }
                ) {
                    LazyColumn {
                        items(s.chat.messages) { message ->
                            Card {
                                Text(message.content)
                            }
                        }
                    }
                }
            }
        }
        ClassroomScreenViewModel.State.Loading -> Loader()
        is ClassroomScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            goToPreviousScreen()
        }
    }
}