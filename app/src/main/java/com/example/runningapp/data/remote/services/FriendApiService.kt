package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.friend.FriendDTO

interface FriendApiService {
    suspend fun fetchFriends(): ApiResponse<List<FriendDTO>>
}