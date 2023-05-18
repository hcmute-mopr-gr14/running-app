package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.IncomingFriend
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.services.FriendApiService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendRemoteDataSource @Inject constructor(private val apiService: FriendApiService) {
    suspend fun fetchFriends(): List<Friend>? =
        when (val response = apiService.fetchFriends()) {
            is ApiResponse.Data ->
                response.data.map {
                    Friend().apply {
                        _id = ObjectId(it._id)
                        email = it.email
                        nickname = it.nickname
                        imageUrl = it.imageUrl
                    }
                }

            else -> null
        }

    suspend fun fetchIncomingFriends(): List<IncomingFriend>? =
        when (val response = apiService.fetchIncomingFriends()) {
            is ApiResponse.Data ->
                response.data.map {
                    IncomingFriend().apply {
                        _id = ObjectId(it._id)
                        email = it.email
                        nickname = it.nickname
                        imageUrl = it.imageUrl
                    }
                }

            else -> null
        }
}
