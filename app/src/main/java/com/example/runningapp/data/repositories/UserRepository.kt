package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.User
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface UserRepository {
    suspend fun getUser(): Flow<SingleQueryChange<User>>
    suspend fun getUser(id: ObjectId): Flow<SingleQueryChange<User>>
}
