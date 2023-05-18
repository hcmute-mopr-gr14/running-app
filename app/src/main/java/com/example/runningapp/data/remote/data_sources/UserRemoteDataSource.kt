package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.services.UserApiService
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val apiService: UserApiService) {
    suspend fun fetchUser(): User? =
        when (val response = apiService.fetchUser()) {
            is ApiResponse.Data ->
                User().apply {
                    _id = ObjectId(response.data._id)
                    nickname = response.data.nickname
                    imageUrl = response.data.imageUrl
                }

            else -> null
        }

    suspend fun fetchUser(id: ObjectId): User? =
        when (val response = apiService.fetchUser(id.toHexString())) {
            is ApiResponse.Data ->
                User().apply {
                    _id = ObjectId(response.data._id)
                    nickname = response.data.nickname
                    email = response.data.email
                    imageUrl = response.data.imageUrl
                }

            else -> null
        }
}
