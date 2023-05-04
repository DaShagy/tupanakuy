package com.dshagapps.tupanakuy.common.util

object Validator {
    fun validateEmail(str: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return emailRegex.matches(str)
    }

    fun validatePassword(str: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()_+-={};':\"|,.<>/?]{8,}\$")
        return passwordRegex.matches(str)
    }
}