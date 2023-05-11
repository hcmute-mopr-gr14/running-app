package com.example.runningapp.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

class Run : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var rounds: RealmList<Round> = realmListOf()

    class Round : EmbeddedRealmObject {
        var points: RealmList<LatLng> = realmListOf()
        var meters: Double = 0.0
        var seconds: Long = 0

        class LatLng : EmbeddedRealmObject {
            var lat: Double = 0.0
            var lng: Double = 0.0
        }
    }
}