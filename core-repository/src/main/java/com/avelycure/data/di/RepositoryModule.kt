package com.avelycure.data.di

import android.content.Context
import com.avelycure.data.local.AppDbHelper
import com.avelycure.data.remote.service.movie.MovieInfoService
import com.avelycure.data.remote.service.movie.PopularMovieService
import com.avelycure.data.remote.service.movie.SearchMovieService
import com.avelycure.data.remote.service.movie.VideoService
import com.avelycure.data.remote.service.person.PersonInfoService
import com.avelycure.data.remote.service.person.PopularPersonService
import com.avelycure.data.remote.service.person.SearchPersonService
import com.avelycure.data.repository.AppRepository
import com.avelycure.domain.repository.IMovieInfoRepository
import com.avelycure.domain.repository.IMovieRepository
import com.avelycure.domain.repository.IPersonRepository
import com.avelycure.domain.repository.IRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun providePersonInfoService(client: HttpClient): PersonInfoService {
        return PersonInfoService(client)
    }

    @Singleton
    @Provides
    fun providePersonService(client: HttpClient): PopularPersonService {
        return PopularPersonService(client)
    }

    @Singleton
    @Provides
    fun provideGetVideoService(client: HttpClient): VideoService {
        return VideoService(client)
    }

    @Singleton
    @Provides
    fun provideSearchMovieService(client: HttpClient): SearchMovieService {
        return SearchMovieService(client)
    }

    @Singleton
    @Provides
    fun provideSearchPersonService(client: HttpClient): SearchPersonService {
        return SearchPersonService(client)
    }

    /**
     * This way we will divide logic and every module will get only needed functionality
     * I checked hash codes, they are the same, so we get the same object every time
     */

    @Provides
    @Singleton
    fun provideRepository(
        popularMovieService: PopularMovieService,
        movieInfoService: MovieInfoService,
        popularPersonService: PopularPersonService,
        videoService: VideoService,
        personInfoService: PersonInfoService,
        searchMovieService: SearchMovieService,
        appDbHelper: AppDbHelper,
        searchPersonService: SearchPersonService
    ): IRepository {
        return AppRepository(
            popularMovieService,
            movieInfoService,
            popularPersonService,
            videoService,
            personInfoService,
            searchMovieService,
            appDbHelper,
            searchPersonService
        )
    }

    @Provides
    @Singleton
    fun provideMovieDbHelper(@ApplicationContext context: Context): AppDbHelper{
        return AppDbHelper(context)
    }

    @Provides
    @Singleton
    fun providePersonRepository(
        repository: IRepository
    ): IPersonRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        repository: IRepository
    ): IMovieRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideMovieInfoRepository(
        repository: IRepository
    ): IMovieInfoRepository {
        return repository
    }
}
