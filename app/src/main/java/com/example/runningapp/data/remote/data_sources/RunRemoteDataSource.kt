package com.example.runningapp.data.remote.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.data.remote.dto.ApiResponse
import com.example.runningapp.data.remote.dto.run.AddRoundRequestDTO
import com.example.runningapp.data.remote.services.RunApiService
import io.realm.kotlin.ext.toRealmList
import kotlinx.datetime.LocalDate
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunRemoteDataSource @Inject constructor(private val runApiService: RunApiService) {
    suspend fun fetchRuns(): List<Run> =
        when (val response = runApiService.fetchRuns()) {
            is ApiResponse.Data ->
                response.data.map {
                    Run().apply {
                        _id = org.mongodb.kbson.BsonObjectId(it._id)
                        rounds = it.rounds.map {
                            Run.Round().apply {
                                points = it.points.map {
                                    Run.Round.LatLng().apply {
                                        lat = it.lat
                                        lng = it.lng
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

    suspend fun addRound(date: LocalDate, round: Run.Round): Boolean =
        when (runApiService.addRound(
            AddRoundRequestDTO(
                date = date,
                round = AddRoundRequestDTO.RoundDTO(
                    points = round.points.map { AddRoundRequestDTO.RoundDTO.LatLngDTO(it.lat, it.lng) },
                    meters = round.meters,
                    seconds = round.seconds
                )
            )
        )) {
            is ApiResponse.Data -> {
                true
            }

            else -> false
        }
}