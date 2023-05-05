package com.dshagapps.tupanakuy.common.navigation

import com.dshagapps.tupanakuy.common.navigation.AppRoutes.AUTH_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.MAIN_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.SPLASH_SCREEN_ROUTE

object AppRoutes {
    const val UID_ARGUMENT = "uid"
    const val SPLASH_SCREEN_ROUTE = "splash"
    const val AUTH_SCREEN_ROUTE = "auth"

    const val MAIN_SCREEN_ROUTE = "main/{uid}"
}

sealed class AppScreen(val route: String) {
    object Splash: AppScreen(SPLASH_SCREEN_ROUTE)
    object Auth: AppScreen(AUTH_SCREEN_ROUTE)
    object Main: AppScreen(MAIN_SCREEN_ROUTE) {
        fun createRoute(uid: String) = "main/${uid}"
    }
}