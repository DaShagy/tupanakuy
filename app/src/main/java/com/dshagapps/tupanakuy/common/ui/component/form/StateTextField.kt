package com.dshagapps.tupanakuy.common.ui.component.form

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dshagapps.tupanakuy.common.ui.util.TextFieldState

@Composable
fun StateTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier,
        value = state.value,
        onValueChange = state.updateValue,
        label = { Text(state.label) }
    )
}