package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Friend
import com.example.runningapp.data.models.IncomingFriend
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FriendLocalDataSource @Inject constructor(private val realm: Realm) {
    fun getFriends(): Flow<List<Friend>> {
        return realm.query<Friend>().asFlow().map { it.list }
    }

    fun getIncomingFriends(): Flow<List<IncomingFriend>> {
        return realm.query<IncomingFriend>().asFlow().map { it.list }
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

    suspend fun upsertIncomingFriends(friends: List<IncomingFriend>) {
        realm.write {
            for (friend in friends) {
                val saved = query<IncomingFriend>("_id == $0", friend._id).first().find()
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

