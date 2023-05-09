package com.example.runningapp.di

import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.remote.DefaultRunningApiService
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import com.example.runningapp.data.repositories.DefaultUserRepository
import com.example.runningapp.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            engine {
                connectTimeout = 100_000
//                socketTimeout = 100_000
//                proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(message = message, tag = "HttpClient")
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json()
            }
            install(HttpCookies)
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }
        }.also { Napier.base(DebugAntilog()) }
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        userLocalDataSource: UserLocalDataSource
    ): UserRepository {
        return DefaultUserRepository(userRemoteDataSource, userLocalDataSource)
    }

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideRunningApiService(@IoDispatcher dispatcher: CoroutineDispatcher, client: HttpClient): RunningApiService =
        DefaultRunningApiService(dispatcher, client)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher
