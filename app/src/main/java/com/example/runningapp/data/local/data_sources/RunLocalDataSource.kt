package com.example.runningapp.data.local.data_sources

import com.example.runningapp.data.models.Run
import com.example.runningapp.di.IoDispatcher
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
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
        return realm.query<Run>().asFlow().map { it.list }
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

    suspend fun addRound(date: LocalDate, round: Run.Round): Run? =
        withContext(dispatcher) {
            val latest = realm.query<Run>().first().find()
            if (latest == null || Instant.fromEpochSeconds(latest._id.timestamp.toLong())
                    .toLocalDateTime(TimeZone.UTC).date < date
            ) {
                return@withContext realm.write {
                    copyToRealm(Run().apply {
                        _id = ObjectId(date.atStartOfDayIn(TimeZone.UTC).epochSeconds)
                        rounds = realmListOf(round)
                    })
                }
            }
            if (Instant.fromEpochSeconds(latest._id.timestamp.toLong()).toLocalDateTime(TimeZone.UTC).date == date) {
                realm.write {
                    latest.rounds.add(round)
                }
                latest
            } else {
                null
            }
        }
}