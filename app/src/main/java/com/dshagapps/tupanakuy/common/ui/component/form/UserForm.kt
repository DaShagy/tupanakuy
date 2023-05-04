package com.dshagapps.tupanakuy.common.ui.component.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dshagapps.tupanakuy.common.ui.component.button.StateButton
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.TextFieldState

@Composable
fun UserForm(
    emailFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    buttonState: ButtonState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            StateTextField(emailFieldState)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            StateTextField(passwordFieldState)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            StateButton(buttonState)
        }
    }
}