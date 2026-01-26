package com.example.universalmotorsporttimingcalenda.data.repository

import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.model.Race
import javax.inject.Inject

class F1RepositoryImpl @Inject constructor(
    private val remoteDataSource: F1DataSource
) : F1Repository {

    override fun observeRaces() = remoteDataSource.observeRaces()

    override suspend fun readAllRaces(): Result<List<Race>> =
        remoteDataSource.readAllRaces()

    override suspend fun readOneRace(round: Int): Result<Race> =
        remoteDataSource.readOneRace(round)
}
