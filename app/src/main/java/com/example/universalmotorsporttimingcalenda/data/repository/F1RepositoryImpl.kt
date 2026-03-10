package com.example.universalmotorsporttimingcalenda.data.repository
import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.model.Race

import com.example.universalmotorsporttimingcalenda.di.LocalDataSource
import com.example.universalmotorsporttimingcalenda.di.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class F1RepositoryImpl @Inject constructor(
    @RemoteDataSource private val remoteDataSource: F1DataSource,
    @LocalDataSource private val localDataSource: F1DataSource,
    private val scope: CoroutineScope
) : F1Repository {

    override fun observeRaces(): Flow<Result<List<Race>>> {
        scope.launch {
            refresh()
        }
        return localDataSource.observeRaces()
    }

    override suspend fun readAllRaces(): Result<List<Race>> =
        remoteDataSource.readAllRaces()

    override suspend fun readOneRace(round: Int): Result<Race> =
        remoteDataSource.readOneRace(round)

    override fun observeOneRace(round: Int): Flow<Result<Race>> {
        scope.launch {
            refresh()
        }
        return localDataSource.observeOneRace(round)
    }

    private suspend fun refresh() {
        val remoteResult = remoteDataSource.readAllRaces()
        if (remoteResult.isSuccess) {
            localDataSource.saveAll(remoteResult.getOrNull()!!)
        }
    }
}

