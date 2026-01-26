package com.example.universalmotorsporttimingcalenda.data.remote

import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.model.Race
import com.example.universalmotorsporttimingcalenda.data.remote.model.toExternal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class F1RemoteDataSource @Inject constructor(
    private val api: F1Api,
    private val scope: CoroutineScope
) : F1DataSource {

    override fun observeRaces(): Flow<Result<List<Race>>> {
        return flow {
            emit(Result.success(emptyList()))
            val result = readAllRaces()
            emit(result)
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000L),
            replay = 1
        )
    }

    override suspend fun readAllRaces(): Result<List<Race>> {
        return try {
            val response = api.getSeason2026()
            if (response.isSuccessful) {
                val body = response.body()!!
                val races = body.mrData.raceTable.races.map { it.toExternal() }
                Result.success(races)
            } else {
                Result.failure(RuntimeException("Error code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readOneRace(round: Int): Result<Race> {
        val all = readAllRaces()
        return if (all.isSuccess) {
            val race = all.getOrNull()?.firstOrNull { it.round == round }
            if (race != null) Result.success(race)
            else Result.failure<Race>(RuntimeException("Race not found"))
        } else {
            Result.failure<Race>(all.exceptionOrNull() ?: RuntimeException("Unknown error"))
        }
    }
}
