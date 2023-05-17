package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dshagapps.tupanakuy.common.navigation.AppRoutesArguments
import com.dshagapps.tupanakuy.common.ui.screen.ClassroomChatScreen
import com.dshagapps.tupanakuy.common.ui.screen.ClassroomScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomChatScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel

object ClassroomSubgraph {

    private const val BASE_ROUTE = "classroom"
    private const val CHAT_ROUTE = "$BASE_ROUTE/chat"

    private const val BASE_SCREEN_ROUTE =
        BASE_ROUTE +
                "/${AppRoutesArguments.USER_UID_ARGUMENT}={${AppRoutesArguments.USER_UID_ARGUMENT}}" +
                "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}={${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}}"

    private const val CHAT_SCREEN_ROUTE =
        CHAT_ROUTE +
                "/${AppRoutesArguments.USER_UID_ARGUMENT}={${AppRoutesArguments.USER_UID_ARGUMENT}}" +
                "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}={${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}}"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ClassroomScreenScaffold(
        navController: NavController,
        userUid: String,
        classroomUid: String,
        content: @Composable  () -> Unit
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Classroom") },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                        navController.navigateToClassroomScreen(userUid, classroomUid)
                    }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Main Classroom")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    IconButton(onClick = {
                        navController.popBackStack()
                        navController.navigateToClassroomChatScreen(userUid, classroomUid)
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Chat")
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it)
            ) {
                content()
            }
        }
    }

    private fun NavGraphBuilder.addClassroomSubgraph(navController: NavController) {

        composable(
            route = Screen.Classroom.route,
            arguments = listOf(
                navArgument(AppRoutesArguments.USER_UID_ARGUMENT) { type = NavType.StringType },
                navArgument(AppRoutesArguments.CLASSROOM_UID_ARGUMENT) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: ClassroomScreenViewModel = hiltViewModel()

            val userUid = backStackEntry.arguments?.getString(AppRoutesArguments.USER_UID_ARGUMENT)
            val classroomUid = backStackEntry.arguments?.getString(AppRoutesArguments.CLASSROOM_UID_ARGUMENT)
            requireNotNull(userUid)
            requireNotNull(classroomUid)

            ClassroomScreenScaffold(navController, userUid, classroomUid) {
                ClassroomScreen(
                    state = viewModel.state,
                    updateState = viewModel::updateState,
                    onInitScreen = {
                        viewModel.getClassroomByIdUseCase(
                            userUid,
                            classroomUid
                        )
                    }
                )
            }
        }

        composable(
            route = Screen.ClassroomChat.route,
            arguments = listOf(
                navArgument(AppRoutesArguments.USER_UID_ARGUMENT) { type = NavType.StringType },
                navArgument(AppRoutesArguments.CLASSROOM_UID_ARGUMENT) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: ClassroomChatScreenViewModel = hiltViewModel()

            val userUid = backStackEntry.arguments?.getString(AppRoutesArguments.USER_UID_ARGUMENT)
            val classroomUid = backStackEntry.arguments?.getString(AppRoutesArguments.CLASSROOM_UID_ARGUMENT)
            requireNotNull(userUid)
            requireNotNull(classroomUid)

            BackHandler {
                navController.popBackStack()
                viewModel.removeChatObserver()
            }

            ClassroomScreenScaffold(navController, userUid, classroomUid)  {
                ClassroomChatScreen(
                    state = viewModel.state,
                    updateState = { newState -> viewModel.updateState(newState) },
                    onInitScreen = { viewModel.getClassroomByIdUseCase(userUid, classroomUid) },
                    onSendButtonClick = { prevState -> viewModel.sendMessageToChat(prevState) },
                    goToPreviousScreen = { navController.popBackStack() }
                )
            }
        }
    }

    fun NavGraphBuilder.addClassroomSubgraphNavigation(navController: NavController) {
        navigation(
            route = BASE_ROUTE,
            startDestination = Screen.Classroom.route
        ) {
            addClassroomSubgraph(navController)
        }
    }

    fun NavController.navigateToClassroomScreen(userUid: String, classroomUid: String) {
        navigate(Screen.Classroom.createRoute(userUid, classroomUid))
    }

    fun NavController.navigateToClassroomChatScreen(userUid: String, classroomUid: String) {
        navigate(Screen.ClassroomChat.createRoute(userUid, classroomUid))
    }

    sealed class Screen(val route: String) {
        object Classroom: Screen(BASE_SCREEN_ROUTE) {
            fun createRoute(userUid: String, classroomUid: String) =
                BASE_ROUTE +
                        "/${AppRoutesArguments.USER_UID_ARGUMENT}=$userUid" +
                        "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}=$classroomUid"
        }

        object ClassroomChat: Screen(CHAT_SCREEN_ROUTE) {
            fun createRoute(userUid: String, classroomUid: String) =
                CHAT_ROUTE +
                        "/${AppRoutesArguments.USER_UID_ARGUMENT}=$userUid" +
                        "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}=$classroomUid"
        }
    }
}