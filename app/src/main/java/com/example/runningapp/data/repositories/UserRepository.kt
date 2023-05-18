package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.User
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUser(): Flow<SingleQueryChange<User>>
}
