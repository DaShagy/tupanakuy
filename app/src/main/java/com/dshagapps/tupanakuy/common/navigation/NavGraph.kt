package com.dshagapps.tupanakuy.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.MainScreenViewModel

fun NavGraphBuilder.addFeedScreenGraph(navController: NavController) {
    composable(route = AppScreen.Main.route) {
        val viewModel: MainScreenViewModel = hiltViewModel()
        MainScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onScreenResume = { viewModel.checkAuthState() }
        )
    }
}