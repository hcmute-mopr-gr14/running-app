package com.example.runningapp.data.remote.services

import com.example.runningapp.data.remote.ApiRoutes
import com.example.runningapp.data.remote.dto.ApiError
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.ApiResponseDTO
import com.example.runningapp.data.remote.dto.user.*
import com.example.runningapp.di.IoDispatcher
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultUserApiService @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val client: HttpClient
) :
    UserApiService {
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

    override suspend fun fetchUser(): ApiResponse<UserResponseDataDTO> {
        return withContext(dispatcher) {
            try {
                val dto: ApiResponseDTO<UserResponseDataDTO> = client.get(ApiRoutes.USER).body()
                dto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error(
                    apiVersion = "--",
                    error = ApiError(code = "EXCEPTION_ERROR", message = "Request failed")
                )
            }
        }
    }

    override suspend fun updateAvatar(imageBytes: ByteArray): ApiResponse<UserResponseDataDTO> {
        return withContext(dispatcher) {
            try {
                val response: HttpResponse = client.submitFormWithBinaryData(
                    url = "",
                    formData = formData {
                        append("image", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(HttpHeaders.ContentDisposition, "filename=\"uploaded_image.png\"")
                        })
                    }
                )

                // Đọc phản hồi từ máy chủ và chuyển đổi nó thành DTO
                val responseBody = response.bodyAsText()
                val responseDto: ApiResponseDTO<UserResponseDataDTO> = Json.decodeFromString(responseBody)

                // Chuyển đổi DTO thành ApiResponse
                responseDto.toApiResponse()
            } catch (e: Exception) {
                ApiResponse.Error(
                    apiVersion = "--",
                    error = ApiError(code = "EXCEPTION_ERROR", message = "Request failed")
                )
            }
        }
    }
}