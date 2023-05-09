package com.dshagapps.tupanakuy.di

import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.auth.domain.use_case.CheckAuthStateUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignInUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignOutUseCase
import com.dshagapps.tupanakuy.auth.domain.use_case.SignUpUseCase
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.dshagapps.tupanakuy.common.domain.use_case.*
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

    //region user_data_use_cases
    @Provides
    fun providesGetUserInfoUseCase(repository: DataRepository) = GetUserInfoUseCase(repository)

    @Provides
    fun providesSetUserInfoUseCase(repository: DataRepository) = SetUserInfoUseCase(repository)

    @Provides
    fun providesSetUserInfoIfNotExistsUseCase(repository: DataRepository) = SetUserInfoIfNotExistsUseCase(repository)
    //endregion

    //region classroom_data_use_cases
    @Provides
    fun providesSetClassroomUseCase(repository: DataRepository) = SetClassroomUseCase(repository)

    @Provides
    fun providesGetClassroomsUseCase(repository: DataRepository) = GetClassroomsUseCase(repository)

    @Provides
    fun providesAddStudentToClassroomUseCase(repository: DataRepository) = AddStudentToClassroomUseCase(repository)

    @Provides
    fun providesRemoveStudentFromClassroomUseCase(repository: DataRepository) = RemoveStudentFromClassroomUseCase(repository)
    //endregion
}