package com.example.runningapp.data.di

import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.IncomingFriend
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.services.*
import com.example.runningapp.data.repositories.*
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
                User::class,
                Run::class,
                Run.Round::class,
                Run.Round.LatLng::class,
                Friend::class,
                IncomingFriend::class
            )
        )
            .schemaVersion(5)
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userRepository: DefaultUserRepository
    ): UserRepository = userRepository

    @Provides
    @Singleton
    fun provideRunRepository(
        runRepository: DefaultRunRepository
    ): RunRepository = runRepository

    @Provides
    @Singleton
    fun provideFriendRepository(
        friendRepository: DefaultFriendRepository
    ): FriendRepository = friendRepository

    @Provides
    @Singleton
    fun provideUserApiService(userApiService: DefaultUserApiService): UserApiService = userApiService

    @Provides
    @Singleton
    fun provideRunApiService(runApiService: DefaultRunApiService): RunApiService = runApiService

    @Provides
    @Singleton
    fun provideFriendApiService(friendApiService: DefaultFriendApiService): FriendApiService = friendApiService
}
