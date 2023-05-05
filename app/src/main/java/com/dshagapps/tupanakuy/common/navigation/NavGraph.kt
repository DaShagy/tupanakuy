package com.dshagapps.tupanakuy.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
            goToAuthScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.Auth.route)
            },
            goToMainScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.Main.route)
            }
        )
    }
    composable(route = AppScreen.Auth.route) {
        val viewModel: AuthScreenViewModel = hiltViewModel()
        AuthScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onScreenResume = { viewModel.checkAuthState() },
            onSignUpButtonClick = { email, password ->
                viewModel.signUp(email, password)
            },
            onSignInButtonClick = { email, password ->
                viewModel.signIn(email, password)
            },
            onSignOutButtonClick = {
                viewModel.signOut()
            }
        )
    }
    composable(route = AppScreen.Main.route) {
        val viewModel: MainScreenViewModel = hiltViewModel()
        MainScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) }
        )
    }
}