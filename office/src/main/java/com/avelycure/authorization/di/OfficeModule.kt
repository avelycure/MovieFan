package com.avelycure.authorization.di

import com.avelycure.authorization.domain.interactors.Login
import com.avelycure.domain.auth.IAuthorizationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OfficeModule {

    @Provides
    @Singleton
    fun provideLoginInteractor(repository: IAuthorizationRepository): Login {
        return Login(repository)
    }
}