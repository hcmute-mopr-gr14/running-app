package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.RunningLog
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getAll(): Flow<List<RunningLog>> {
        return realm.query<RunningLog>().asFlow().map { it.list }
    }

    suspend fun upsert(logs: List<RunningLog>) {
        realm.write {
            for (log in logs) {
                val saved = query<RunningLog>("_id == $0", log._id).first().find()
                if (saved != null) {
                    saved.distance = log.distance
                    saved.seconds = log.seconds
                    saved.steps = log.steps
                } else {
                    copyToRealm(log)
                }
            }
        }
    }
}