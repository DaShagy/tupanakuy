package com.dshagapps.tupanakuy.common.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dshagapps.tupanakuy.R
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreen

@Composable
fun MainScreen(name: String) {
    BaseScreen(title = stringResource(id = R.string.app_name) ) {
        Text(text = "Hello $name!")
    }
}