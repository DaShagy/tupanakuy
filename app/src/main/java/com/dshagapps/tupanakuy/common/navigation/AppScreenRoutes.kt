package com.dshagapps.tupanakuy.common.navigation

import com.dshagapps.tupanakuy.common.navigation.AppRoutes.AUTH_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.CLASSROOM_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.MAIN_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.SPLASH_SCREEN_ROUTE

object AppRoutes {

    //Arguments
    const val USER_UID_ARGUMENT = "userUid"
    const val CLASSROOM_UID_ARGUMENT = "classroomUid"

    // Screen routes
    const val SPLASH_SCREEN_ROUTE = "splash"
    const val AUTH_SCREEN_ROUTE = "auth"

    // Subgraph routes
    const val MAIN_SUBGRAPH_ROUTE = "main"
    const val CLASSROOM_SUBGRAPH_ROUTE = "classroom"

    const val MAIN_SCREEN_ROUTE =
        "$MAIN_SUBGRAPH_ROUTE/$USER_UID_ARGUMENT={$USER_UID_ARGUMENT}"
    const val CLASSROOM_SCREEN_ROUTE =
        "$CLASSROOM_SUBGRAPH_ROUTE/$USER_UID_ARGUMENT={$USER_UID_ARGUMENT}&$CLASSROOM_UID_ARGUMENT={$CLASSROOM_UID_ARGUMENT}"
}

sealed class AppScreen(val route: String) {
    object Splash: AppScreen(SPLASH_SCREEN_ROUTE)
    object Auth: AppScreen(AUTH_SCREEN_ROUTE)
    object Main: AppScreen(MAIN_SCREEN_ROUTE) {
        fun createRoute(userUid: String) =
            AppRoutes.MAIN_SUBGRAPH_ROUTE +
                    "/${AppRoutes.USER_UID_ARGUMENT}=$userUid"
    }
    object Classroom: AppScreen(CLASSROOM_SCREEN_ROUTE) {
        fun createRoute(userUid: String, classroomUid: String) =
            AppRoutes.CLASSROOM_SUBGRAPH_ROUTE +
                    "/${AppRoutes.USER_UID_ARGUMENT}=$userUid" +
                    "&${AppRoutes.CLASSROOM_UID_ARGUMENT}=$classroomUid"
    }
}