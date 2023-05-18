package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.IncomingFriend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    suspend fun getFriends(): Flow<List<Friend>>
    suspend fun getIncomingFriends(): Flow<List<IncomingFriend>>
}
