package com.dshagapps.tupanakuy.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.UID_ARGUMENT
import com.dshagapps.tupanakuy.common.ui.screen.AuthScreen
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen
import com.dshagapps.tupanakuy.common.ui.screen.SplashScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.AuthScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.SplashScreenViewModel

fun NavGraphBuilder.addFeedScreenGraph(navController: NavController) {
    composable(route = AppScreen.Splash.route) {
        val viewModel: SplashScreenViewModel = hiltViewModel()
        SplashScreen(
            state = viewModel.state,
            checkAuthState = { viewModel.checkAuthState() },
            goToAuthScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.Auth.route)
            },
            goToMainScreen = { uid ->
                navController.popBackStack()
                navController.navigate(AppScreen.Main.createRoute(uid))
            }
        )
    }
    composable(route = AppScreen.Auth.route) {
        val viewModel: AuthScreenViewModel = hiltViewModel()
        AuthScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onSignUpButtonClick = { email, password ->
                viewModel.signUp(email, password)
            },
            onSignInButtonClick = { email, password ->
                viewModel.signIn(email, password)
            },
            goToMainScreen = { uid ->
                navController.popBackStack()
                navController.navigate(AppScreen.Main.createRoute(uid))
            }
        )
    }
    composable(
        route = AppScreen.Main.route,
        arguments = listOf(
            navArgument(UID_ARGUMENT) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val viewModel: MainScreenViewModel = hiltViewModel()

        val uid = backStackEntry.arguments?.getString(UID_ARGUMENT)
        requireNotNull(uid)

        MainScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onInitScreen = { viewModel.getUserInfo(uid) },
            onSignOutButtonClick = { viewModel.signOut() },
            onToggleUserTypeButtonClick = { user -> viewModel.updateUserInfo(user) },
            goToAuthScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.Auth.route)
            },
        )
    }
}