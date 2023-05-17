package com.example.runningapp.data.remote.dto.user

import android.provider.ContactsContract.CommonDataKinds.Nickname
import kotlinx.serialization.Serializable

@Serializable
data class UserRequestDTO(
    val nickname: String? = null,
    val currentPassword: String,
    val newPassword: String
)