package com.dshagapps.tupanakuy.common.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen

fun NavGraphBuilder.addFeedScreenGraph(navController: NavController) {
    composable(route = AppScreen.Main.route) {
        MainScreen("Android")
    }
}