package com.dshagapps.tupanakuy.common.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: StateFlow<MainScreenViewModel.State>,
    updateState: (MainScreenViewModel.State) -> Unit = {},
    onInitScreen: () -> Unit = {},
    onSignOutButtonClick: () -> Unit = {},
    onCreateClassroomButtonClick: (MainScreenViewModel.State.Idle) -> Unit = {},
    onClassroomSignUpButtonClick: (String, MainScreenViewModel.State.Idle) -> Unit = { _, _ -> },
    onClassroomSignOutButtonClick: (String, MainScreenViewModel.State.Idle) -> Unit = { _, _ -> },
    goToAuthScreen: () -> Unit = {},
    goToClassroomScreen: (String) -> Unit = {},
    goToProfileScreen: () -> Unit = {}
) {
    val context: Context = LocalContext.current

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> onInitScreen()
            else -> Unit
        }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    when (val s = state.collectAsState().value) {
        is MainScreenViewModel.State.Idle -> {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        StateButton(
                            modifier = Modifier.fillMaxWidth(),
                            state = ButtonState(
                                label = "My classrooms",
                                onClick = {
                                    //TODO: Redirect to classrooms
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        StateButton(
                            modifier = Modifier.fillMaxWidth(),
                            state = ButtonState(
                                label = "Profile",
                                onClick = {
                                    goToProfileScreen()
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        StateButton(
                            modifier = Modifier.fillMaxWidth(),
                            state = ButtonState(
                                label = "Settings",
                                onClick = {
                                    //TODO: Redirect to settings
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        StateButton(
                            modifier = Modifier.fillMaxWidth(),
                            state = ButtonState(
                                label = "Sign out",
                                onClick = {
                                    onSignOutButtonClick()
                                    coroutineScope.launch { drawerState.close() }
                                }
                            )
                        )
                    }
                }
            ) {
                BaseScreen(
                    title = stringResource(id = R.string.app_name)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("UID: ${s.user.uid}")
                        Text("Email: ${s.user.email}")
                        Text("You are a: ${s.user.userType}")
                        if (s.user.userType == UserType.TEACHER) {
                            StateButton(
                                ButtonState(
                                    label = "Create classroom",
                                    onClick = {
                                        updateState(MainScreenViewModel.State.OnCreateClassroom(s))
                                    }
                                )
                            )
                        }
                        LazyColumn{
                            items(s.classrooms){
                                Card(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(it.uid)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        if (s.user.userType != UserType.TEACHER) {
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
                                        if (it.studentUIDs.contains(s.user.uid)) {
                                            StateButton(
                                                ButtonState(
                                                    label = "Unregister",
                                                    onClick = {
                                                        updateState(
                                                            MainScreenViewModel.State.OnClassroomSignOut(it.uid, s)
                                                        )
                                                    },
                                                    enabled = (s.user.userType == UserType.STUDENT && it.studentUIDs.contains(s.user.uid))
                                                )
                                            )
                                        }
                                    }
                                    if (it.studentUIDs.contains(s.user.uid) || it.teacherUID == s.user.uid) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            StateButton(
                                                ButtonState(
                                                    label = "Go to classroom screen",
                                                    onClick = {
                                                        updateState(
                                                            MainScreenViewModel.State.GoToClassroom(it.uid)
                                                        )
                                                    }
                                                )
                                            )
                                        }
                                    }
                                }
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
        is MainScreenViewModel.State.OnClassroomSignOut -> {
            onClassroomSignOutButtonClick(s.classroomUid, s.prevState)
        }
        is MainScreenViewModel.State.OnAuthError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
        is MainScreenViewModel.State.OnError -> {
            Toast.makeText(context, s.exception.message, Toast.LENGTH_SHORT).show()
            updateState(MainScreenViewModel.State.Idle(s.prevState.user, s.prevState.classrooms))
        }
        is MainScreenViewModel.State.GoToClassroom -> {
            updateState(MainScreenViewModel.State.Loading)
            LaunchedEffect(Unit) {
                goToClassroomScreen(s.classroomUid)
            }
        }
    }
}