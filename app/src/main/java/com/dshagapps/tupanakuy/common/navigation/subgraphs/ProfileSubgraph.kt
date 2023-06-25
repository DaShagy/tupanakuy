package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dshagapps.tupanakuy.common.navigation.AppRoutesArguments
import com.dshagapps.tupanakuy.common.ui.screen.ProfileScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.ProfileScreenViewModel

object ProfileSubgraph {

    const val BASE_ROUTE = "profile"

    private const val BASE_SCREEN_ROUTE =
        "${BASE_ROUTE}/${AppRoutesArguments.USER_UID_ARGUMENT}={${AppRoutesArguments.USER_UID_ARGUMENT}}"

    fun NavGraphBuilder.addProfileScreen(navController: NavController) {
        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument(AppRoutesArguments.USER_UID_ARGUMENT) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userUid = backStackEntry.arguments?.getString(AppRoutesArguments.USER_UID_ARGUMENT)
            requireNotNull(userUid)

            val viewModel: ProfileScreenViewModel = hiltViewModel()
            ProfileScreen(
                state = viewModel.state,
                onInitScreen = { viewModel.getUserInfo(userUid) },
                goToPreviousScreen = { navController.popBackStack() }
            )
        }
    }

    fun NavController.navigateToProfileScreen(uid: String) {
        navigate(route = Screen.Profile.createRoute(uid))
    }

    sealed class Screen(val route: String) {
        object Profile: Screen(BASE_SCREEN_ROUTE) {
            fun createRoute(userUid: String) =
                BASE_ROUTE +
                        "/${AppRoutesArguments.USER_UID_ARGUMENT}=$userUid"
        }
    }
}