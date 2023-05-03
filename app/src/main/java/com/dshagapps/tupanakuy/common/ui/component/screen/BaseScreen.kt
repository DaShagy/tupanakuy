package com.dshagapps.tupanakuy.common.ui.component.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreenId.BUTTONS_LAYOUT_ID
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreenId.CONTENT_LAYOUT_ID
import com.dshagapps.tupanakuy.common.ui.component.screen.BaseScreenId.TITLE_LAYOUT_ID
import com.dshagapps.tupanakuy.common.ui.theme.TupanakuyTheme
import com.dshagapps.tupanakuy.common.ui.util.ThemeProvider

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String = "",
    content: @Composable RowScope.() -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
        constraintSet = getBaseScreenConstraintSet()
    ) {
        if (title != "") {
            Row(
                modifier = modifier
                    .layoutId(TITLE_LAYOUT_ID)
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = title)
            }
        }
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
            modifier = modifier.layoutId(BUTTONS_LAYOUT_ID)
        ) {
            Text(text = title)
        }
    }
}

@Preview
@Composable
fun NoButtonBaseScreenPreview(
    @PreviewParameter(
        provider = ThemeProvider::class
    ) isDarkTheme: Boolean
) {
    TupanakuyTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BaseScreen(
                title = "Title"
            ) {
                Text("Content")
            }
        }
    }
}

private fun getBaseScreenConstraintSet() = ConstraintSet {
    val topLayout = createRefFor(TITLE_LAYOUT_ID)
    val contentLayout = createRefFor(CONTENT_LAYOUT_ID)
    val bottomLayout = createRefFor(BUTTONS_LAYOUT_ID)

    createVerticalChain(topLayout, contentLayout, bottomLayout)

    constrain(topLayout) {
        start.linkTo(parent.start)
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        bottom.linkTo(contentLayout.top)
    }
    constrain(contentLayout) {
        start.linkTo(parent.start)
        top.linkTo(topLayout.bottom)
        end.linkTo(parent.end)
        bottom.linkTo(bottomLayout.top)
        height = Dimension.fillToConstraints
    }
    constrain(bottomLayout) {
        start.linkTo(parent.start)
        top.linkTo(contentLayout.bottom)
        end.linkTo(parent.end)
        bottom.linkTo(parent.bottom)
        width = Dimension.fillToConstraints
    }
}

private object BaseScreenId {
    const val TITLE_LAYOUT_ID = "TITLE_LAYOUT_ID"
    const val CONTENT_LAYOUT_ID = "CONTENT_LAYOUT_ID"
    const val BUTTONS_LAYOUT_ID = "BUTTONS_LAYOUT_ID"
}