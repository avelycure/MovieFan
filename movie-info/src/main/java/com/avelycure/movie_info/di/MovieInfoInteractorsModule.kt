package com.avelycure.movie_info.di

import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.movie_info.domain.interactors.GetMovieInfo
import com.avelycure.movie_info.domain.interactors.GetTrailerCode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieInfoInteractorsModule {
    @Provides
    @Singleton
    fun provideGetMovieInfo(
        repository: IMovieInfoRepository
    ): GetMovieInfo {
        return GetMovieInfo(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrailerCode(
        repository: IMovieInfoRepository
    ): GetTrailerCode {
        return GetTrailerCode(repository)
    }
}