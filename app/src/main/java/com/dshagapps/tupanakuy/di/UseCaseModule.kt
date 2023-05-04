package com.dshagapps.tupanakuy.di

import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.auth.domain.use_case.CheckAuthStateUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignInUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    //region auth_use_cases
    @Provides
    fun providesCheckAuthStateUseCase(repository: AuthRepository) = CheckAuthStateUseCase(repository)

    @Provides
    fun providesSignUpUseCase(repository: AuthRepository) = SignUpUseCase(repository)

    @Provides
    fun providesSignOutUseCase(repository: AuthRepository) = SignOutUseCase(repository)

    @Provides
    fun providesSignInUseCase(repository: AuthRepository) = SignInUseCase(repository)
    //endregion
}