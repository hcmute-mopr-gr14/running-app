package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.ApiRoutes
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.ApiResponseDTO
import com.example.runningapp.data.remote.dto.friend.FriendDTO
import com.example.runningapp.data.remote.dto.friend.FriendRequestRequestDTO
import com.example.runningapp.data.remote.dto.friend.FriendRequestResponseDTO
import com.example.runningapp.di.IoDispatcher
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultFriendApiService @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val client: HttpClient
) :
    FriendApiService {
    override suspend fun fetchFriends(): ApiResponse<List<FriendDTO>> {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<List<FriendDTO>> = client.get(ApiRoutes.USER_FRIENDS).body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error(
                    apiVersion = "--",
                    error = ApiError(code = "EXCEPTION_ERROR", message = "Request failed")
                )
            }
        }
    }

    override suspend fun fetchIncomingFriends(): ApiResponse<List<FriendDTO>> {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<List<FriendDTO>> = client.get(ApiRoutes.USER_FRIENDS_REQUESTS_INCOMING).body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error(
                    apiVersion = "--",
                    error = ApiError(code = "EXCEPTION_ERROR", message = "Request failed")
                )
            }
        }
    }

    override suspend fun postFriendRequest(body: FriendRequestRequestDTO): ApiResponse<FriendRequestResponseDTO> =
        withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<FriendRequestResponseDTO> = client.post(ApiRoutes.USER_FRIENDS_REQUESTS) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error(
                    apiVersion = "--",
                    error = ApiError(code = "EXCEPTION_ERROR", message = "Request failed")
                )
            }
        }
}