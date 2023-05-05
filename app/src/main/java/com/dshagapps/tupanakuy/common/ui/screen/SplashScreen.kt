package com.dshagapps.tupanakuy.common.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.ui.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SplashScreen(
    state: StateFlow<SplashScreenViewModel.State>,
    checkAuthState: () -> Unit,
    goToAuthScreen: () -> Unit = {},
    goToMainScreen: (String) -> Unit = {}
) {

    when (val s = state.collectAsState().value) {
        is SplashScreenViewModel.State.Start -> {
            StartSplashScreen {
                checkAuthState()
            }
        }
        SplashScreenViewModel.State.GoToAuthScreen -> {
            LaunchedEffect(Unit) {
                goToAuthScreen()
            }
        }
        is SplashScreenViewModel.State.GoToMainScreen -> {
            LaunchedEffect(Unit) {
                goToMainScreen(s.uid)
            }
        }
    }
}

@Composable
fun StartSplashScreen(
    checkAuthState: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
    }

    LaunchedEffect(Unit) {
        delay(3000)
        checkAuthState()
    }
}