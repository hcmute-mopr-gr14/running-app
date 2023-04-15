package com.example.runningapp.data.remote.repositories

import com.example.runningapp.data.remote.ApiRoutes
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiUserRepository @Inject constructor(private val client: HttpClient) : UserRepository {
    override suspend fun login(body: LoginRequest): ApiResponse<LoginResponseData>? {
        return withContext(Dispatchers.IO) {
            try {
                client.post(ApiRoutes.LOGIN) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
            } catch (e: Exception) {
                null
            }
        }
    }
}