package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.notifications.SingleQueryChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getFriends(): Flow<List<Friend>> {
        return realm.query<Friend>().asFlow().map { it.list }
    }

    suspend fun upsertFriends(friends: List<Friend>) {
        realm.write {
            for (friend in friends) {
                val saved = query<Friend>("_id == $0", friend._id).first().find()
                if (saved != null) {
                    saved.email = friend.email
                    saved.nickname = friend.nickname
                    saved.imageUrl = friend.imageUrl
                } else {
                    copyToRealm(friend)
                }
            }
        }
    }
}

