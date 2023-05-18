package com.example.runningapp.data.repositories

import android.util.Log
import com.example.runningapp.data.local.data_sources.FriendLocalDataSource
import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.remote.data_sources.FriendRemoteDataSource
import com.example.runningapp.data.remote.data_sources.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFriendRepository @Inject constructor(
    private val friendRemoteDataSource: FriendRemoteDataSource,
    private val friendLocalDataSource: FriendLocalDataSource,
) : FriendRepository {
    private val friends: Flow<List<Friend>> = friendLocalDataSource.getFriends()

    override suspend fun getFriends(): Flow<List<Friend>> = supervisorScope {
        launch {
            try {
                val users = friendRemoteDataSource.fetchFriends()
                if (users != null) {
                    friendLocalDataSource.upsertFriends(users)
                }
            } catch (e: Exception) {
                Log.d("UserRepository", "Connection to remote failed, using local data source")
                Log.d("UserRepository", e.toString())
            }
        }
        friends
    }
}
