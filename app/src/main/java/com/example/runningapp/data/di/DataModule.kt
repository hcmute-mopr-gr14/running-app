package com.example.runningapp.data.di

import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.models.RunningLog
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.DefaultRunningApiService
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import com.example.runningapp.data.repositories.DefaultUserRepository
import com.example.runningapp.data.repositories.UserRepository
import com.example.runningapp.di.IoDispatcher
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
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
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
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                User::class, RunningLog::class
            )
        )
            .schemaVersion(3)
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()
        return Realm.open(config)
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
    @Singleton
    fun provideRunningApiService(@IoDispatcher dispatcher: CoroutineDispatcher, client: HttpClient): RunningApiService =
        DefaultRunningApiService(dispatcher, client)
}
