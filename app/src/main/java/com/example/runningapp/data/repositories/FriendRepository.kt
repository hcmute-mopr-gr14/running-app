package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    suspend fun getFriends(): Flow<List<Friend>>
}
