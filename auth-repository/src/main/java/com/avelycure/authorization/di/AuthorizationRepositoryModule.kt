package com.avelycure.authorization.di

import com.avelycure.authorization.AuthorizationRepository
import com.avelycure.authorization.service.RequestTokenService
import com.avelycure.domain.auth.IAuthorizationRepository
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
object AuthorizationRepositoryModule {
    /*@Singleton
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
    }*/

    @Provides
    @Singleton
    fun provideRequestTokenService(client: HttpClient):RequestTokenService{
        return RequestTokenService(client)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(requestTokenService: RequestTokenService):IAuthorizationRepository{
        return AuthorizationRepository(
            requestTokenService = requestTokenService
        )
    }
}