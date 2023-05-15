package com.dshagapps.tupanakuy.common.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.AUTH_SUBGRAPH_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.CLASSROOM_SUBGRAPH_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.CLASSROOM_UID_ARGUMENT
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.MAIN_SUBGRAPH_ROUTE
import com.dshagapps.tupanakuy.common.navigation.AppRoutes.USER_UID_ARGUMENT
import com.dshagapps.tupanakuy.common.ui.screen.AuthScreen
import com.dshagapps.tupanakuy.common.ui.screen.ClassroomScreen
import com.dshagapps.tupanakuy.common.ui.screen.MainScreen
import com.dshagapps.tupanakuy.common.ui.screen.SplashScreen
import com.dshagapps.tupanakuy.common.ui.viewmodel.*

fun NavGraphBuilder.addSplashScreen(navController: NavController) {
    composable(route = AppScreen.Splash.route) {
        val viewModel: SplashScreenViewModel = hiltViewModel()
        SplashScreen(
            state = viewModel.state,
            checkAuthState = { viewModel.checkAuthState() },
            goToAuthScreen = {
                navController.popBackStack()
                navController.navigate(AppScreen.SignIn.route)
            },
            goToMainScreen = { uid ->
                navController.popBackStack()
                navController.navigate(AppScreen.Main.createRoute(uid))
            }
        )
    }
}

fun NavGraphBuilder.addAuthSubgraph(navController: NavController) {
    composable(route = AppScreen.SignIn.route) {
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
                navController.navigate(AppScreen.SignUp.route)
            },
            goToMainScreen = { uid ->
                navController.popBackStack()
                navController.navigate(AppScreen.Main.createRoute(uid))
            }
        )
    }
    composable(route = AppScreen.SignUp.route) {
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
                navController.navigate(AppScreen.SignIn.route)
            },
            goToMainScreen = { uid ->
                navController.popBackStack()
                navController.navigate(AppScreen.Main.createRoute(uid))
            }
        )
    }
}

fun NavGraphBuilder.addMainSubgraph(navController: NavController) {
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
                navController.navigate(AppScreen.SignIn.route)
            },
            goToClassroomScreen = { classroomUid ->
                navController.navigate(AppScreen.Classroom.createRoute(userUid, classroomUid))
            }
        )
    }
}

fun NavGraphBuilder.addClassroomSubgraph(navController: NavController) {
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

fun NavGraphBuilder.addFeedScreenGraph(navController: NavController) {
    addSplashScreen(navController)

    navigation(
        route = AUTH_SUBGRAPH_ROUTE,
        startDestination = AppScreen.SignIn.route
    ) {
        addAuthSubgraph(navController)
    }

    navigation(
        route = MAIN_SUBGRAPH_ROUTE,
        startDestination = AppScreen.Main.route
    ) {
        addMainSubgraph(navController)
    }

    navigation(
        route = CLASSROOM_SUBGRAPH_ROUTE,
        startDestination = AppScreen.Classroom.route
    ) {
        addClassroomSubgraph(navController)
    }
}