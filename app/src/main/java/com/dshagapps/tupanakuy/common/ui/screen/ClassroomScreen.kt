package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.component.screen.ChatScreen
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ClassroomScreen(
    state: StateFlow<ClassroomScreenViewModel.State>,
    updateState: (ClassroomScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSendButtonClick: (ClassroomScreenViewModel.State.Idle) -> Unit = {},
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
            ClassroomChatScreen(state = s, updateState = updateState)
        }
        ClassroomScreenViewModel.State.Loading -> Loader()
        is ClassroomScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            goToPreviousScreen()
        }
        is ClassroomScreenViewModel.State.OnSendMessageButtonClick -> {
            onSendButtonClick(s.prevState)
            ClassroomChatScreen(state = s.prevState, updateState = updateState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClassroomChatScreen(
    state: ClassroomScreenViewModel.State.Idle,
    updateState: (ClassroomScreenViewModel.State) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val hideKeyboard: () -> Unit = { keyboardController?.hide() }

    BaseScreen(
        title = "Classroom: ${state.classroom.uid}, Chat: ${state.classroom.chatUID}"
    ) {
        ChatScreen(
            messageFieldState = state.messageFieldState,
            sendMessageButtonState = ButtonState(
                label = "Send",
                onClick = {
                    hideKeyboard()
                    if (state.messageFieldState.value.isNotEmpty()) {
                        updateState(ClassroomScreenViewModel.State.OnSendMessageButtonClick(state))
                    }
                }
            )
        ) {
            LazyColumn {
                items(state.chat.messages) { message ->
                    Card {
                        Text(message.content)
                    }
                }
            }
        }
    }
}