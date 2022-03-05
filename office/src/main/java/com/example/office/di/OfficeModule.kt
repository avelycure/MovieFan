package com.example.office.di

import com.example.office.domain.interactors.Login
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
    fun provideLogin(authRepository: IAuthorizationRepository):Login{
        return Login(authRepository = authRepository)
    }
}