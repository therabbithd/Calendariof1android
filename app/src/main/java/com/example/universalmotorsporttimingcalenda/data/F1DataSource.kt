package com.example.universalmotorsporttimingcalenda.data

import com.example.universalmotorsporttimingcalenda.data.model.Race
import kotlinx.coroutines.flow.Flow

interface F1DataSource {
    fun observeRaces(): Flow<Result<List<Race>>>
    suspend fun readAllRaces(): Result<List<Race>>
    suspend fun readOneRace(round: Int): Result<Race>
}
