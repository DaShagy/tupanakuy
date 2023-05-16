package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.dshagapps.tupanakuy.common.navigation.AppRoutesArguments
import com.dshagapps.tupanakuy.common.navigation.subgraphs.AuthSubgraph.navigateToSignInScreen
import com.dshagapps.tupanakuy.common.navigation.subgraphs.ClassroomSubgraph.navigateToClassroomChatScreen
import com.dshagapps.tupanakuy.common.navigation.subgraphs.ClassroomSubgraph.navigateToClassroomScreen
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel

object MainSubgraph {

    private const val MAIN_SUBGRAPH_ROUTE = "main"

    private const val MAIN_SCREEN_ROUTE =
        "$MAIN_SUBGRAPH_ROUTE/${AppRoutesArguments.USER_UID_ARGUMENT}={${AppRoutesArguments.USER_UID_ARGUMENT}}"


    private fun NavGraphBuilder.addMainSubgraph(navController: NavController) {
        composable(
            route = Screen.Main.route,
            arguments = listOf(
                navArgument(AppRoutesArguments.USER_UID_ARGUMENT) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: MainScreenViewModel = hiltViewModel()

            val userUid = backStackEntry.arguments?.getString(AppRoutesArguments.USER_UID_ARGUMENT)
            requireNotNull(userUid)

            MainScreen(
                state = viewModel.state,
                updateState = { newState -> viewModel.updateState(newState) },
                onInitScreen = { viewModel.updateScreenData(userUid) },
                onSignOutButtonClick = { viewModel.signOut() },
                onCreateClassroomButtonClick = { prevState ->
                    viewModel.createClassroom(prevState)
                },
                onClassroomSignUpButtonClick = { classroomUid, prevState ->
                    viewModel.classroomSignUp(classroomUid, prevState)
                },
                onClassroomSignOutButtonClick = { classroomUid, prevState ->
                    viewModel.classroomSignOut(classroomUid, prevState)
                },
                goToAuthScreen = {
                    navController.popBackStack()
                    navController.navigateToSignInScreen()
                },
                goToClassroomScreen = { classroomUid ->
                    navController.navigateToClassroomScreen(userUid, classroomUid)
                }
            )
        }
    }

    fun NavGraphBuilder.addMainSubgraphNavigation(navController: NavController) {
        navigation(
            route = MAIN_SUBGRAPH_ROUTE,
            startDestination = Screen.Main.route
        ) {
            addMainSubgraph(navController)
        }
    }

    fun NavController.navigateToMainScreen(userUid: String) {
        navigate(Screen.Main.createRoute(userUid))
    }

    sealed class Screen(val route: String) {
        object Main: Screen(MAIN_SCREEN_ROUTE) {
            fun createRoute(userUid: String) =
                MAIN_SUBGRAPH_ROUTE +
                        "/${AppRoutesArguments.USER_UID_ARGUMENT}=$userUid"
        }
    }
}