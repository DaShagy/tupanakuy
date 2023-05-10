package com.dshagapps.tupanakuy.common.navigation

import com.dshagapps.tupanakuy.common.navigation.AppRoutes.AUTH_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.CLASSROOM_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.MAIN_SCREEN_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.SPLASH_SCREEN_ROUTE

object AppRoutes {
    const val USER_UID_ARGUMENT = "userUid"
    const val CLASSROOM_UID_ARGUMENT = "classroomUid"
    const val SPLASH_SCREEN_ROUTE = "splash"
    const val AUTH_SCREEN_ROUTE = "auth"

    const val MAIN_SCREEN_ROUTE = "main/$USER_UID_ARGUMENT={$USER_UID_ARGUMENT}"
    const val CLASSROOM_SCREEN_ROUTE = "classroom/$USER_UID_ARGUMENT={$USER_UID_ARGUMENT}&$CLASSROOM_UID_ARGUMENT={$CLASSROOM_UID_ARGUMENT}"
}

sealed class AppScreen(val route: String) {
    object Splash: AppScreen(SPLASH_SCREEN_ROUTE)
    object Auth: AppScreen(AUTH_SCREEN_ROUTE)
    object Main: AppScreen(MAIN_SCREEN_ROUTE) {
        fun createRoute(userUid: String) = "main/${AppRoutes.USER_UID_ARGUMENT}=$userUid"
    }
    object Classroom: AppScreen(CLASSROOM_SCREEN_ROUTE) {
        fun createRoute(userUid: String, classroomUid: String) =
            "classroom/${AppRoutes.USER_UID_ARGUMENT}=$userUid&${AppRoutes.CLASSROOM_UID_ARGUMENT}=$classroomUid"
    }
}