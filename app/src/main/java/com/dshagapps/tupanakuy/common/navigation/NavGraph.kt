package com.dshagapps.tupanakuy.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.CLASSROOM_UID_ARGUMENT
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.USER_UID_ARGUMENT
import com.dshagapps.tupanakuy.common.ui.screen.AuthScreen
import com.dshagapps.tupanakuy.common.ui.screen.ClassroomScreen
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen
import com.dshagapps.tupanakuy.common.ui.screen.SplashScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.AuthScreenViewModel
import com.dshagapps.tupanakuy.common.ui.viewmodel.ClassroomScreenViewModel
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
            navArgument(USER_UID_ARGUMENT) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val viewModel: MainScreenViewModel = hiltViewModel()

        val userUid = backStackEntry.arguments?.getString(USER_UID_ARGUMENT)
        requireNotNull(userUid)

        MainScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onInitScreen = { viewModel.updateScreenData(userUid) },
            onSignOutButtonClick = { viewModel.signOut() },
            onCreateClassroomButtonClick = { prevState ->
                viewModel.createClassroom(prevState)
            },
            onClassroomSignUpButtonClick = { classroomUid, prevState ->
                viewModel.classroomSignUp(classroomUid, prevState)
            },
            onClassroomSignOutButtonClick = { classroomUid, prevState ->
                viewModel.classroomSignOut(classroomUid, prevState)
            },
            goToAuthScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.Auth.route)
            },
            goToClassroomScreen = { classroomUid ->
                navController.navigate(AppScreen.Classroom.createRoute(userUid, classroomUid))
            }
        )
    }
    composable(
        route = AppScreen.Classroom.route,
        arguments = listOf(
            navArgument(USER_UID_ARGUMENT) { type = NavType.StringType },
            navArgument(CLASSROOM_UID_ARGUMENT) { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val viewModel: ClassroomScreenViewModel = hiltViewModel()

        val userUid = backStackEntry.arguments?.getString(USER_UID_ARGUMENT)
        val classroomUid = backStackEntry.arguments?.getString(CLASSROOM_UID_ARGUMENT)
        requireNotNull(userUid)
        requireNotNull(classroomUid)

        ClassroomScreen(
            state = viewModel.state,
            updateState = { newState -> viewModel.updateState(newState) },
            onInitScreen = { viewModel.getClassroomByIdUseCase(userUid, classroomUid) },
            onSendButtonClick = { prevState -> viewModel.sendMessageToChat(prevState) },
            goToPreviousScreen = { navController.popBackStack() }
        )
    }
}