package com.example.runningapp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RunningLog : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var seconds: Int = 0
    var steps: Int = 0
    var distance: Float = 0f
}