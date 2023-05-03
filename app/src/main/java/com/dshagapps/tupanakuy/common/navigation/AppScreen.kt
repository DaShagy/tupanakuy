package com.dshagapps.tupanakuy.common.navigation

import com.dshagapps.tupanakuy.common.navigation.AppRoutes.MAIN_SCREEN_ROUTE

object AppRoutes {
    const val MAIN_SCREEN_ROUTE = "main"
}

sealed class AppScreen(val route: String) {
    object Main: AppScreen(MAIN_SCREEN_ROUTE)
}