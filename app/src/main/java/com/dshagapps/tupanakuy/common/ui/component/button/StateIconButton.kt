package com.dshagapps.tupanakuy.common.ui.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dshagapps.tupanakuy.common.ui.util.ButtonState

@Composable
fun StateIconButton(
    state: ButtonState,
    modifier: Modifier = Modifier
) {
    IconButton(
        modifier = modifier,
        onClick = state.onClick,
        enabled = state.enabled
    ) {
        when (state.label) {
            "Send" -> Icon(imageVector = Icons.Default.Send, contentDescription = "Chat")
            else -> Text(text = state.label)
        }
    }
}
