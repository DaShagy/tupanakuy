package com.dshagapps.tupanakuy.common.ui.viewmodel

import com.dshagapps.tupanakuy.auth.domain.use_case.SignInUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.common.domain.use_case.SetUserInfoIfNotExistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    signInUseCase: SignInUseCase,
    signOutUseCase: SignOutUseCase,
    setUserInfoIfNotExistsUseCase: SetUserInfoIfNotExistsUseCase
): AuthScreenViewModel(
    signInUseCase,
    signOutUseCase,
    setUserInfoIfNotExistsUseCase
) {
    override fun getViewModelType(): Type =
        Type.SignIn
}