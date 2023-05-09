package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.dto.ApiResponse
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRemoteDataSource @Inject constructor(private val apiService: RunningApiService) {
    suspend fun fetchRunningLogs(): List<Run> =
        listOf()
//        when (val response = apiService.fetchHomeData()) {
//            is ApiResponse.Data ->
//                response.data.runningLogs.map {
//                    Run().apply {
//                        _id = ObjectId(it._id)
//                        seconds = it.seconds
//                        steps = it.steps
//                        distance = it.distance
//                    }
//                }
//
//            else -> listOf()
//        }

    suspend fun fetchRuns(): List<Run> =
        when (val response = apiService.fetchRuns()) {
            is ApiResponse.Data ->
                response.data.map {
                    Run().apply {
                        _id = ObjectId(it._id)
                        rounds = it.rounds.map {
                            Run.Round().apply {
                                points = it.points.map {
                                    Run.Round.LatLng().apply {
                                        lat = it.getOrElse(0) { 0.0 }
                                        lng = it.getOrElse(0) { 0.0 }
                                    }
                                }.toRealmList()
                                meters = it.meters
                                seconds = it.seconds
                            }
                        }.toRealmList()
                    }
                }

            else -> listOf()
        }
}