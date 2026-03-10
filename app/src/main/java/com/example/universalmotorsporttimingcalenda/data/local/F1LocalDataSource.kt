package com.example.universalmotorsporttimingcalenda.data.local

import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.model.Race
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class F1LocalDataSource @Inject constructor(
    private val raceDao: RaceDao
) : F1DataSource {

    override fun observeRaces(): Flow<Result<List<Race>>> {
        return raceDao.observeAll().map { entities ->
            Result.success(entities.toModel())
        }
    }

    override suspend fun readAllRaces(): Result<List<Race>> {
        return try {
            val entities = raceDao.getAll()
            Result.success(entities.toModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readOneRace(round: Int): Result<Race> {
        return try {
            val entity = raceDao.getRaceByRound(round)
            if (entity != null) {
                Result.success(entity.toModel())
            } else {
                Result.failure(RaceNotFoundException(round))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveAll(races: List<Race>) {
        raceDao.insertAll(races.map { it.toEntity() })
    }

    override fun observeOneRace(round: Int): Flow<Result<Race>> {
        return raceDao.observeAll().map { entities ->
            val race = entities.find { it.round == round }?.toModel()
            if (race != null) Result.success(race)
            else Result.failure(RaceNotFoundException(round))
        }
    }
}
