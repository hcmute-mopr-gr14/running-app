package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.di.IoDispatcher
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.query.Sort
import io.realm.kotlin.query.find
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.*
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RunLocalDataSource @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val realm: Realm
) {
    fun getAll(): Flow<List<Run>> {
        return realm.query<Run>().sort(property = "_id", sortOrder = Sort.DESCENDING).asFlow().map { it.list }
    }

    suspend fun upsertRuns(runs: List<Run>) {
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

    suspend fun addRound(date: LocalDate, round: Run.Round): Boolean =
        withContext(dispatcher) {
            val runs = realm.query<Run>().find()
            for (run in runs) {
                println(
                    "${
                        Instant.fromEpochSeconds(run._id.timestamp.toLong()).toLocalDateTime(TimeZone.UTC).date
                    } == $date"
                )
                if (Instant.fromEpochSeconds(run._id.timestamp.toLong()).toLocalDateTime(TimeZone.UTC).date == date) {
                    realm.write {
                        val latest = findLatest(run)
                        latest?.rounds?.add(round)
                    }
                    println("update $run")
                    return@withContext true
                }
            }
            realm.write {
                copyToRealm(Run().apply {
                    _id = ObjectId(date.atStartOfDayIn(TimeZone.UTC).epochSeconds)
                    rounds = realmListOf(round)
                })
                println("add $round")
            }
            return@withContext true
        }
}