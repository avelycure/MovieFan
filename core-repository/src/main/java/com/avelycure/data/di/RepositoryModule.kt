package com.avelycure.data.di

import com.avelycure.data.remote.service.PopularMovieService
import com.avelycure.data.repository.MovieRepository
import com.avelycure.domain.repository.IMovieRepository
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
    fun provideMovieRepository(popularMovieService: PopularMovieService): IMovieRepository {
        return MovieRepository(popularMovieService)
    }
}