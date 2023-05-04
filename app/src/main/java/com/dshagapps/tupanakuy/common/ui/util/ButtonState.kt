package com.dshagapps.tupanakuy.common.ui.util

data class ButtonState(
    val label: String = "",
    val onClick: () -> Unit = {},
    val enabled: Boolean = true
)
