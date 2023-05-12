package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {}