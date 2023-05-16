package com.dshagapps.tupanakuy.common.ui.viewmodel

import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignUpUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetUserInfoIfNotExistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    signUpUseCase: SignUpUseCase,
    signOutUseCase: SignOutUseCase,
    setUserInfoIfNotExistsUseCase: SetUserInfoIfNotExistsUseCase
): AuthScreenViewModel(
    signUpUseCase,
    signOutUseCase,
    setUserInfoIfNotExistsUseCase
) {
    override fun getViewModelType(): Type =
        Type.SignUp
}