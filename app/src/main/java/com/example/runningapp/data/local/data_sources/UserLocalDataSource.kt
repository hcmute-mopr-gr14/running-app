package com.example.runningapp.data.local.data_sources

import io.realm.kotlin.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val realm: Realm)