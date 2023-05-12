package com.example.runningapp.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class User : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var email: String = ""
    var password: String = ""
    var nickname: String = ""
    var imageUrl: String = ""
    var runs: RealmList<Run> = realmListOf()
}
