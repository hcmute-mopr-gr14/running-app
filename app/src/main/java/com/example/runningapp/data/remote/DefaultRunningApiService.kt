package com.example.runningapp.data.remote

import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.ApiResponseDTO
import com.example.runningapp.data.remote.dto.user.*
import com.example.runningapp.di.IoDispatcher
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultRunningApiService @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val client: HttpClient) : RunningApiService {
    override suspend fun login(body: LoginRequestDTO): ApiResponse<LoginResponseDataDTO>? {
        return withContext(dispatcher) {
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
    override suspend fun signup(body: SignupRequestDTO): ApiResponse<SignupResponseDataDTO>? {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<SignupResponseDataDTO> = client.post(ApiRoutes.SIGN_UP) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
                dto.toApiResponse()
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun fetchHomeData(): ApiResponse<HomeResponseDataDTO>? {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<HomeResponseDataDTO> = client.get(ApiRoutes.HOME) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }.body()
                dto.toApiResponse()
            } catch (e: Exception) {
                null
            }
        }
    }
}