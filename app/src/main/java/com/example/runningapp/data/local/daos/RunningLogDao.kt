package com.example.runningapp.data.local.daos

import androidx.room.*
import com.example.runningapp.data.entities.RunningLog
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningLogDao {
    @Query("SELECT * FROM running_logs")
    fun getAll(): Flow<List<RunningLog>>

    @Upsert
    suspend fun upsert(vararg logs: RunningLog)

    @Insert
    suspend fun insert(vararg logs: RunningLog)

    @Delete
    suspend fun delete(vararg logs: RunningLog): Int
}