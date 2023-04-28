package com.example.runningapp.data.remote.dto

import kotlinx.serialization.*

//@Serializable(with = ApiResponseSerializer::class)
sealed class ApiResponse<out T : Any> {
    abstract val apiVersion: String
    data class Data<out T: Any>(override val apiVersion: String, val data: T) : ApiResponse<T>()
    data class Error(override val apiVersion: String, val error: ApiError) : ApiResponse<Nothing>()
}

@Serializable
data class ApiResponseDTO<out T: Any>(
    val apiVersion: String,
    val data: T? = null,
    val error: ApiError? = null
) {
    fun toApiResponse() : ApiResponse<T> {
        if (data != null) {
            return ApiResponse.Data(apiVersion = this.apiVersion, data = this.data)
        }
        if (error != null) {
            return ApiResponse.Error(apiVersion = this.apiVersion, error = this.error)
        }
        return ApiResponse.Error(apiVersion = this.apiVersion, error = ApiError(code = "EXCEPTION", message = "JSON conversion failed"))
    }
}

@Serializable
data class ApiError(
    val code: String,
    val message: String
)

//@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
//class ApiResponseSerializer<T : Any>(private val dataSerializer: KSerializer<T>) : KSerializer<ApiResponse<T>> {
//    override val descriptor: SerialDescriptor = buildSerialDescriptor("ApiResponse", PolymorphicKind.SEALED) {
//        element("Data", buildClassSerialDescriptor("Data") {
//            element<String>("message")
//        })
//        element("Error", dataSerializer.descriptor)
//    }
//
//    override fun deserialize(decoder: Decoder): ApiResponse<T> {
//        require(decoder is JsonDecoder)
//        val element = decoder.decodeJsonElement()
//        if (element is JsonObject && "error" in element) {
//            if ("error" in element) {
//                return ApiResponse.Error(
//                    apiVersion = element["apiVersion"]!!.jsonPrimitive.content,
//                    error = ApiError(
//                        code = element["error"]!!.jsonObject["code"]!!.jsonPrimitive.content.toInt(),
//                        message = element["error"]!!.jsonObject["message"]!!.jsonPrimitive.content
//                    )
//                )
//            }
//            if ("data" in element) {
//                return ApiResponse.Data(
//                    apiVersion = element["apiVersion"]!!.jsonPrimitive.content,
//                    data = decoder.json.decodeFromJsonElement(dataSerializer, element["data"]!!)
//                )
//            }
//        }
//        return ApiResponse.Error("", ApiError(code = 1, message = "Cannot decode json"))
//    }
//
//    override fun serialize(encoder: Encoder, value: ApiResponse<T>) {
//        require(encoder is JsonEncoder)
//        val element = when (value) {
//            is ApiResponse.Data -> buildJsonObject {
//                put(key = "apiVersion", value = value.apiVersion)
//                put(key = "data", element = encoder.json.encodeToJsonElement(dataSerializer, value.data))
//            }
//
//            is ApiResponse.Error -> buildJsonObject {
//                put(key = "apiVersion", value = value.apiVersion)
//                put(key = "error", element = encoder.json.encodeToJsonElement(value.error))
//            }
//        }
//        encoder.encodeJsonElement(element)
//    }
//}
