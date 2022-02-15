package com.avelycure.movie_info.di

import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.movie_info.domain.interactors.GetMovieInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MovieInfoInteractorsModule {
    @Provides
    fun provideGetMovieInfo(
        repository: IMovieInfoRepository
    ): GetMovieInfo {
        return GetMovieInfo(repository)
    }
}