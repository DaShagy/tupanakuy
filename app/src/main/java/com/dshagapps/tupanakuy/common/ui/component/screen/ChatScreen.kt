package com.dshagapps.tupanakuy.common.ui.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.dshagapps.tupanakuy.common.ui.component.button.StateButton
import com.dshagapps.tupanakuy.common.ui.component.form.StateTextField
import com.dshagapps.tupanakuy.common.ui.component.screen.ChatScreenId.CONTENT_LAYOUT_ID
import com.dshagapps.tupanakuy.common.ui.component.screen.ChatScreenId.MESSAGE_INPUT_LAYOUT_ID
import com.dshagapps.tupanakuy.common.ui.util.ButtonState
import com.dshagapps.tupanakuy.common.ui.util.TextFieldState

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    messageFieldState: TextFieldState = TextFieldState(),
    sendMessageButtonState: ButtonState = ButtonState(),
    content: @Composable RowScope.() -> Unit
) {

    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
        constraintSet = getChatScreenConstraintSet()
    ) {
        Row(
            modifier = modifier
                .layoutId(CONTENT_LAYOUT_ID)
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
        Row(
            modifier = modifier
                .layoutId(MESSAGE_INPUT_LAYOUT_ID)
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StateTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                state = messageFieldState
            )
            StateButton(sendMessageButtonState)
        }
    }
}

private fun getChatScreenConstraintSet() = ConstraintSet {
    val contentLayout = createRefFor(CONTENT_LAYOUT_ID)
    val bottomLayout = createRefFor(MESSAGE_INPUT_LAYOUT_ID)

    createVerticalChain(contentLayout, bottomLayout)

    constrain(contentLayout) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        bottom.linkTo(bottomLayout.top)
        height = Dimension.fillToConstraints
    }
    constrain(bottomLayout) {
        start.linkTo(parent.start)
        top.linkTo(contentLayout.bottom)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
    }
}

private object ChatScreenId {
    const val CONTENT_LAYOUT_ID = "CONTENT_LAYOUT_ID"
    const val MESSAGE_INPUT_LAYOUT_ID = "MESSAGE_INPUT_LAYOUT_ID"
}