package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Run
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getAllRuns(): Flow<List<Run>> {
        return realm.query<Run>().asFlow().map { it.list }
    }

    suspend fun upsert(runs: List<Run>) {
        realm.write {
            for (run in runs) {
                val saved = query<Run>("_id == $0", run._id).first().find()
                if (saved != null) {
                    saved.rounds = run.rounds.toRealmList()
                } else {
                    copyToRealm(run)
                }
            }
        }
    }
}