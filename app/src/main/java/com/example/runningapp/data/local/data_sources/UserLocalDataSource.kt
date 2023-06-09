package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getUser(): Flow<SingleQueryChange<User>> {
        return realm.query<User>().first().asFlow()
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

    suspend fun upsertUser(user: User) {
        realm.write {
            val saved = query<User>("_id == $0", user._id).first().find()
            if (saved != null) {
                saved.imageUrl = user.imageUrl
                saved.nickname = user.nickname
            } else {
                copyToRealm(user)
            }
        }
    }

}

