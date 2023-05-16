package com.dshagapps.tupanakuy.common.navigation.subgraphs

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.dshagapps.tupanakuy.common.navigation.subgraphs.MainSubgraph.navigateToMainScreen
import com.dshagapps.tupanakuy.common.ui.screen.AuthScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.SignInScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.SignUpScreenViewModel

object AuthSubgraph {

    private const val AUTH_SUBGRAPH_ROUTE = "auth"

    private const val SIGN_IN_SCREEN_ROUTE = "$AUTH_SUBGRAPH_ROUTE/signIn"
    private const val SIGN_UP_SCREEN_ROUTE = "$AUTH_SUBGRAPH_ROUTE/signUp"

    private fun NavGraphBuilder.addAuthSubgraph(navController: NavController) {
        composable(route = Screen.SignIn.route) {
            val viewModel: SignInScreenViewModel = hiltViewModel()
            AuthScreen(
                type = viewModel.getViewModelType(),
                state = viewModel.state,
                updateState = { newState -> viewModel.updateState(newState) },
                onFormButtonClick = { prevState ->
                    viewModel.auth(prevState)
                },
                onChangeScreenButtonClick = {
                    navController.popBackStack()
                    navController.navigateToSignUpScreen()
                },
                goToMainScreen = { uid ->
                    navController.popBackStack()
                    navController.navigateToMainScreen(uid)
                }
            )
        }
        composable(route = Screen.SignUp.route) {
            val viewModel: SignUpScreenViewModel = hiltViewModel()
            AuthScreen(
                type = viewModel.getViewModelType(),
                state = viewModel.state,
                updateState = { newState -> viewModel.updateState(newState) },
                onFormButtonClick = { prevState ->
                    viewModel.auth(prevState)
                },
                onChangeScreenButtonClick = {
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

    fun NavGraphBuilder.addAuthSubgraphNavigation(navController: NavController) {
        navigation(
            route = AUTH_SUBGRAPH_ROUTE,
            startDestination = Screen.SignIn.route
        ) {
            addAuthSubgraph(navController)
        }
    }

    fun NavController.navigateToSignInScreen() {
        navigate(Screen.SignIn.route)
    }

    fun NavController.navigateToSignUpScreen() {
        navigate(Screen.SignUp.route)
    }

    sealed class Screen(val route: String) {
        object SignIn: Screen(SIGN_IN_SCREEN_ROUTE)
        object SignUp: Screen(SIGN_UP_SCREEN_ROUTE)
    }
}