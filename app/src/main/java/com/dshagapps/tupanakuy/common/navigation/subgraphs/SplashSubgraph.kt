package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dshagapps.tupanakuy.common.navigation.subgraphs.AuthSubgraph.navigateToSignInScreen
import com.dshagapps.tupanakuy.common.navigation.subgraphs.MainSubgraph.navigateToMainScreen
import com.dshagapps.tupanakuy.common.ui.screen.SplashScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.SplashScreenViewModel

object SplashSubgraph {

    const val SPLASH_SCREEN_ROUTE = "splash"

    fun NavGraphBuilder.addSplashScreen(navController: NavController) {
        composable(route = Screen.Splash.route) {
            val viewModel: SplashScreenViewModel = hiltViewModel()
            SplashScreen(
                state = viewModel.state,
                checkAuthState = { viewModel.checkAuthState() },
                goToAuthScreen = {
                    navController.popBackStack()
                    navController.navigateToSignInScreen()
                },
                goToMainScreen = { uid ->
                    navController.popBackStack()
                    navController.navigateToMainScreen(uid)
                }
            )
        }
    }

    sealed class Screen(val route: String) {
        object Splash: Screen(SPLASH_SCREEN_ROUTE)
    }
}