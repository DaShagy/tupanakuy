package com.dshagapps.tupanakuy.common.ui.component.button

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dshagapps.tupanakuy.common.ui.util.ButtonState

@Composable
fun StateButton(
    state: ButtonState,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = state.onClick,
        enabled = state.enabled
    ) {
        Text(state.label)
    }
}