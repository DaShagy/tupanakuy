package com.dshagapps.tupanakuy.di

import com.dshagapps.tupanakuy.auth.data.repository.AuthRepositoryImpl
import com.dshagapps.tupanakuy.auth.domain.repository.AuthRepository
import com.dshagapps.tupanakuy.common.data.repository.DataRepositoryImpl
import com.dshagapps.tupanakuy.common.domain.repository.DataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    fun provideDataRepository(firestore: FirebaseFirestore): DataRepository = DataRepositoryImpl(firestore)
}