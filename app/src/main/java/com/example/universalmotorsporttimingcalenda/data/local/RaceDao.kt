package com.example.universalmotorsporttimingcalenda.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(race: RaceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(races: List<RaceEntity>): List<Long>

    @Query("SELECT * FROM races")
    suspend fun getAll(): List<RaceEntity>

    @Query("SELECT * FROM races")
    fun observeAll(): Flow<List<RaceEntity>>

    @Query("SELECT * FROM races WHERE round = :round")
    suspend fun getRaceByRound(round: Int): RaceEntity?
}
