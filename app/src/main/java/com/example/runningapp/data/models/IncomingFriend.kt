package com.example.runningapp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class IncomingFriend : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var email: String = ""
    var nickname: String = ""
    var imageUrl: String = ""
}
