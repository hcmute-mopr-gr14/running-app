package com.example.runningapp.data.di

import com.example.runningapp.data.models.RunningLog
import com.example.runningapp.data.models.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.realm.kotlin.InitialDataCallback
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
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
}
