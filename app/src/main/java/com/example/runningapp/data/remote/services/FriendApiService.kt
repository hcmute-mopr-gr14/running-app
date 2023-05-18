package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.friend.FriendDTO
import com.example.runningapp.data.remote.dto.friend.FriendRequestRequestDTO
import com.example.runningapp.data.remote.dto.friend.FriendRequestResponseDTO

interface FriendApiService {
    suspend fun fetchFriends(): ApiResponse<List<FriendDTO>>
    suspend fun postFriendRequest(body: FriendRequestRequestDTO): ApiResponse<FriendRequestResponseDTO>
}