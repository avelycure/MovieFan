package com.avelycure.image_loader.di

import android.content.Context
import com.avelycure.image_loader.ImageLoader
import com.avelycure.image_loader.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {
    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context):ImageLoader{
        return ImageLoader(context, R.drawable.placeholder)
    }
}