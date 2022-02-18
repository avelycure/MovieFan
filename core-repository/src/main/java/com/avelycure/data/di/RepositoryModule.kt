package com.avelycure.data.di

import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.repository.MovieRepository
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.domain.repository.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSerializer(): KotlinxSerializer {
        return KotlinxSerializer(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }

    @Singleton
    @Provides
    fun provideClient(customSerializer: KotlinxSerializer): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = customSerializer
            }
        }
    }

    @Singleton
    @Provides
    fun providePopularMovieService(client: HttpClient): PopularMovieService {
        return PopularMovieService(client)
    }

    @Singleton
    @Provides
    fun provideMovieInfoService(client: HttpClient): MovieInfoService {
        return MovieInfoService(client)
    }

    /**
     * This way we will divide logic and every module will get only needed functionality
     * I checked hash codes, they are the same, so we get the same object every time
     */

    @Provides
    @Singleton
    fun provideRepository(
        popularMovieService: PopularMovieService,
        movieInfoService: MovieInfoService
    ): IRepository {
        return MovieRepository(popularMovieService, movieInfoService)
    }

    @Provides
    fun provideMovieRepository(
        repository: IRepository
    ): IMovieRepository {
        return repository
    }

    @Provides
    fun provideMovieInfoRepository(
        repository: IRepository
    ): IMovieInfoRepository {
        return repository
    }
}