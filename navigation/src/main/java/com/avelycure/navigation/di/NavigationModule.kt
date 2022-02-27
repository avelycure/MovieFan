package com.avelycure.navigation.di

import com.avelycure.core_navigation.Navigator
import com.avelycure.navigation.Compas
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule{
    @Provides
    @Singleton
    fun provideNavigator(): Navigator{
        return Compas()
    }
}