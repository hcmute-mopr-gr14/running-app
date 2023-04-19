package com.example.runningapp.data.remote.repositories

import com.example.runningapp.data.remote.ApiRoutes
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.ApiResponseDTO
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
    override suspend fun login(body: LoginRequestDTO): ApiResponse<LoginResponseDataDTO>? {
        return withContext(Dispatchers.IO) {
            try {
               val dto: ApiResponseDTO<LoginResponseDataDTO> = client.post(ApiRoutes.LOGIN) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
                dto.toApiResponse()
            } catch (e: Exception) {
                null
            }
        }
    }
    override suspend fun signup(body: SignupRequest): ApiResponse<SignupResponseData>? {
        return withContext(Dispatchers.IO) {
            try {
                client.post(ApiRoutes.SIGN_UP) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
            } catch (e: Exception) {
                null
            }
        }
    }
}