package com.dshagapps.tupanakuy.common.ui.component.loader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.dshagapps.tupanakuy.common.ui.theme.TupanakuyTheme
import com.dshagapps.tupanakuy.common.ui.util.ThemeProvider

@Composable
fun Loader() {
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun LoaderPreview(
    @PreviewParameter(
        provider = ThemeProvider::class
    ) isDarkTheme: Boolean
) {
    TupanakuyTheme(darkTheme = isDarkTheme) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Loader()
        }
    }
}