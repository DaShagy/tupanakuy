package com.dshagapps.tupanakuy.common.ui.util

import androidx.compose.runtime.mutableStateOf

data class TextFieldState(
    private val _initialState: String = "",
    private val _initialErrorState: Boolean = false,
    private val _onValueChangedCallback: (String) -> Unit = {},
    private val _validator: (String) -> Boolean = { false },
    val label: String = ""
) {
    val state = mutableStateOf(_initialState)
    val errorState = mutableStateOf(_initialErrorState)

    val value get() = state.value
    val isError: Boolean get() = errorState.value

    val updateValue: (String) -> Unit = { newValue ->
        state.value = newValue
        errorState.value = _validator(newValue)
        _onValueChangedCallback(newValue)
    }
}