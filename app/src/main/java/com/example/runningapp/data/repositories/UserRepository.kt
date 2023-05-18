package com.example.runningapp.data.repositories

import com.example.runningapp.data.models.User
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface UserRepository {
    suspend fun getUser(id: ObjectId): Flow<User?>
}
