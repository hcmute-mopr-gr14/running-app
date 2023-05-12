package com.example.runningapp.data.remote.data_sources

<<<<<<< Updated upstream
=======
import com.example.runningapp.data.models.Run
import com.example.runningapp.data.models.User
import com.example.runningapp.data.remote.RunningApiService
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.user.UserRequestDTO
import com.example.runningapp.data.remote.dto.user.UserResponseDataDTO
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.ObjectId
>>>>>>> Stashed changes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
<<<<<<< Updated upstream
class UserRemoteDataSource @Inject constructor()
=======
class UserRemoteDataSource @Inject constructor(private val apiService: RunningApiService) {
    /*suspend fun fetchRunningLogs(): List<Run> =
        listOf()
        when (val response = apiService.fetchHomeData()) {
            is ApiResponse.Data ->
                response.data.runningLogs.map {
                    Run().apply {
                        _id = ObjectId(it._id)
                        seconds = it.seconds
                        steps = it.steps
                        distance = it.distance
                    }
                }

            else -> listOf()
        }*/

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

    suspend fun fetchUser(): List<User> =
        when (val response = apiService.fetchUser()) {
            is ApiResponse.Data ->
                response.data.map {
                    User().apply {
                        _id = ObjectId(it._id)
                        nickname = it.nickname
                        imageUrl = it.imageUrl
                        runs = it.runs.map {
                            Run().apply {
                                _id = ObjectId(it._id)
                                rounds = it.rounds.map {
                                    Run.Round().apply {
                                        meters = it.meters
                                        seconds = it.seconds
                                    }
                                }.toRealmList()
                            }
                        }.toRealmList()
                    }
                }
            else -> listOf()
        }

}
>>>>>>> Stashed changes
