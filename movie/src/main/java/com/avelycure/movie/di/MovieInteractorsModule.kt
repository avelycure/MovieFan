package com.avelycure.movie.di

import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.movie.domain.interactors.GetPopularMovies
import com.avelycure.movie.domain.interactors.SearchMovie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieInteractorsModule {

    @Singleton
    @Provides
    fun provideGetPopularMovies(repository: IMovieRepository): GetPopularMovies {
        return GetPopularMovies(repository)
    }

    @Singleton
    @Provides
    fun provideSearchMovie(repository: IMovieRepository): SearchMovie {
        return SearchMovie(repository)
    }
}