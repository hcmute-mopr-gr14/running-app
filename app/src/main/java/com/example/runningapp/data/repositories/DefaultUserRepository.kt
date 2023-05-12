package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.UserRequestDTO
import com.example.runningapp.data.remote.dto.user.UserResponseDataDTO
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    private val user: Flow<SingleQueryChange<User>> = userLocalDataSource.getUser()

    override suspend fun getUser(): Flow<SingleQueryChange<User>> = supervisorScope {
        launch {
            try {
                val user = remoteDataSource.fetchUser()
                if (user != null) {
                    userLocalDataSource.upsertUser(user)
                }
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        user
    }
}
