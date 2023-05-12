package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import io.realm.kotlin.Realm
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getAllRuns(): Flow<List<Run>> {
        return realm.query<Run>().asFlow().map { it.list }
    }
    fun getUser(): Flow<List<User>> {
        return realm.query<User>().asFlow().map { it.list }
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

    suspend fun upsertUser(users: List<User>) {
        realm.write {
            for (user in users) {
                val saved = query<User>("_id == $0", user._id).first().find()
                if (saved != null) {
                    saved.imageUrl = user.imageUrl
                    saved.nickname = user.nickname
                    saved.runs = user.runs.toRealmList()
                } else {
                    copyToRealm(user)
                }
            }
        }
    }

}

