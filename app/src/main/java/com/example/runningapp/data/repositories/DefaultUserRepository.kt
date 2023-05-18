package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.local.data_sources.UserLocalDataSource
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    private val user: Flow<SingleQueryChange<User>> = userLocalDataSource.getUser()

    override suspend fun getUser(): Flow<SingleQueryChange<User>> = supervisorScope {
        launch {
            try {
                val user = userRemoteDataSource.fetchUser()
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

    override suspend fun getUser(id: ObjectId): Flow<SingleQueryChange<User>> = supervisorScope {
        val user = userLocalDataSource.getUser(id)
        launch {
            try {
                val remoteUser = userRemoteDataSource.fetchUser(id)
                if (remoteUser != null) {
                    userLocalDataSource.upsertUser(remoteUser)
                }
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        user
    }
}
