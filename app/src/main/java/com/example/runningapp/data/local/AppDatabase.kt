package com.example.runningapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.runningapp.data.entities.RunningLog
import com.example.runningapp.data.entities.User
import com.example.runningapp.data.local.daos.RunningLogDao

//@Database(version = 2, entities = [User::class, RunningLog::class], autoMigrations = [AutoMigration(from = 1, to = 2)])
@Database(version = 2, entities = [User::class, RunningLog::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun runningLogDao(): RunningLogDao
}