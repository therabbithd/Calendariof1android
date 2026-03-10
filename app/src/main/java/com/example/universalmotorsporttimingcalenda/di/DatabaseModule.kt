package com.example.universalmotorsporttimingcalenda.di

import android.content.Context
import androidx.room.Room
import com.example.universalmotorsporttimingcalenda.data.local.F1Database
import com.example.universalmotorsporttimingcalenda.data.local.RaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): F1Database {
        return Room.databaseBuilder(
            context,
            F1Database::class.java,
            "f1_database"
        ).build()
    }

    @Provides
    fun provideRaceDao(database: F1Database): RaceDao {
        return database.raceDao()
    }
}
