package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dshagapps.tupanakuy.common.navigation.AppRoutesArguments
import com.dshagapps.tupanakuy.common.ui.screen.ClassroomScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel

object ClassroomSubgraph {

    private const val CLASSROOM_SUBGRAPH_ROUTE = "classroom"

    private const val CLASSROOM_SCREEN_ROUTE =
        CLASSROOM_SUBGRAPH_ROUTE +
                "/${AppRoutesArguments.USER_UID_ARGUMENT}={${AppRoutesArguments.USER_UID_ARGUMENT}}" +
                "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}={${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}}"

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

            BackHandler {
                navController.popBackStack()
                viewModel.removeChatObserver()
            }

            ClassroomScreen(
                state = viewModel.state,
                updateState = { newState -> viewModel.updateState(newState) },
                onInitScreen = { viewModel.getClassroomByIdUseCase(userUid, classroomUid) },
                onSendButtonClick = { prevState -> viewModel.sendMessageToChat(prevState) },
                goToPreviousScreen = { navController.popBackStack() }
            )
        }
    }

    fun NavGraphBuilder.addClassroomSubgraphNavigation(navController: NavController) {
        navigation(
            route = CLASSROOM_SUBGRAPH_ROUTE,
            startDestination = Screen.Classroom.route
        ) {
            addClassroomSubgraph(navController)
        }
    }

    fun NavController.navigateToClassroomScreen(userUid: String, classroomUid: String) {
        navigate(Screen.Classroom.createRoute(userUid, classroomUid))
    }

    sealed class Screen(val route: String) {
        object Classroom: Screen(CLASSROOM_SCREEN_ROUTE) {
            fun createRoute(userUid: String, classroomUid: String) =
                CLASSROOM_SUBGRAPH_ROUTE +
                        "/${AppRoutesArguments.USER_UID_ARGUMENT}=$userUid" +
                        "&${AppRoutesArguments.CLASSROOM_UID_ARGUMENT}=$classroomUid"
        }
    }
}