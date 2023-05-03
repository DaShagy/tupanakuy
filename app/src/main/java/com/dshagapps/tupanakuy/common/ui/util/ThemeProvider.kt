package com.dshagapps.tupanakuy.common.ui.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ThemeProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(false, true)
}