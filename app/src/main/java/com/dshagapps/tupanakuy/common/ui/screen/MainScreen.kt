package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.domain.model.enum.UserType
import com.dshagapps.tupanakuy.common.ui.component.button.StateButton
import com.dshagapps.tupanakuy.common.ui.component.loader.Loader
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.OnLifecycleEvent
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: StateFlow<MainScreenViewModel.State>,
    updateState: (MainScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSignOutButtonClick: () -> Unit = {},
    onCreateClassroomButtonClick: (MainScreenViewModel.State.Idle) -> Unit = {},
    onClassroomSignUpButtonClick: (String, MainScreenViewModel.State.Idle) -> Unit = { _, _ -> },
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
                    Text("You are a: ${s.user.userType}")
                    StateButton(
                        ButtonState(
                            label = "Create classroom",
                            onClick = { updateState(MainScreenViewModel.State.OnCreateClassroom(s)) },
                            enabled = s.user.userType == UserType.TEACHER
                        )
                    )
                    LazyColumn{
                        items(s.classrooms){
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(it.uid)
                                StateButton(
                                    ButtonState(
                                        label = "Sign up to Classroom",
                                        onClick = {
                                            updateState(
                                                MainScreenViewModel.State.OnClassroomSignUp(it.uid, s)
                                            )
                                        },
                                        enabled = (s.user.userType == UserType.STUDENT && !it.studentUIDs.contains(s.user.uid))
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        MainScreenViewModel.State.Loading -> Loader()
        MainScreenViewModel.State.OnSignOut -> {
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
        is MainScreenViewModel.State.OnCreateClassroom -> {
            onCreateClassroomButtonClick(s.prevState)
        }
        is MainScreenViewModel.State.OnClassroomSignUp -> {
            onClassroomSignUpButtonClick(s.classroomUid, s.prevState)
        }
        is MainScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
    }
}