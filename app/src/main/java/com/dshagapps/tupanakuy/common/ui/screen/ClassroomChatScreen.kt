package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.component.screen.ChatScreen
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomChatScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ClassroomChatScreen(
    state: StateFlow<ClassroomChatScreenViewModel.State>,
    updateState: (ClassroomChatScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSendButtonClick: (ClassroomChatScreenViewModel.State.Idle) -> Unit = {},
    goToPreviousScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            Lifecycle.Event.ON_PAUSE -> updateState(ClassroomChatScreenViewModel.State.Loading)
            else -> Unit
        }
    }

    when (val s = state.collectAsState().value) {
        is ClassroomChatScreenViewModel.State.Idle -> {
            ClassroomChatScreen(state = s, updateState = updateState)
        }
        ClassroomChatScreenViewModel.State.Loading -> Loader()
        is ClassroomChatScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            goToPreviousScreen()
        }
        is ClassroomChatScreenViewModel.State.OnSendMessageButtonClick -> {
            onSendButtonClick(s.prevState)
            ClassroomChatScreen(state = s.prevState, updateState = updateState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClassroomChatScreen(
    state: ClassroomChatScreenViewModel.State.Idle,
    updateState: (ClassroomChatScreenViewModel.State) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard: () -> Unit = { keyboardController?.hide() }

    BaseScreen(title = "Classroom chat") {
        ChatScreen(
            messageFieldState = state.messageFieldState,
            sendMessageButtonState = ButtonState(
                label = "Send",
                onClick = {
                    hideKeyboard()
                    if (state.messageFieldState.value.isNotEmpty()) {
                        updateState(ClassroomChatScreenViewModel.State.OnSendMessageButtonClick(state))
                    }
                }
            )
        ) {
            LazyColumn {
                items(state.chat.messages) { message ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 4.dp),
                        horizontalArrangement = if (message.authorUID == state.currentUserUID) Arrangement.End else Arrangement.Start
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .wrapContentWidth(if (message.authorUID == state.currentUserUID) Alignment.End else Alignment.Start)
                        ) {
                            Row(modifier = Modifier.padding(4.dp)) {
                                Text(message.content)
                            }
                        }
                    }
                }
            }
        }
    }
}