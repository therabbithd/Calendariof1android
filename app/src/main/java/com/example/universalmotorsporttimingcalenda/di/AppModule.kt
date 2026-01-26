package com.example.universalmotorsporttimingcalenda.di

import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.remote.F1RemoteDataSource
import com.example.universalmotorsporttimingcalenda.data.repository.F1Repository
import com.example.universalmotorsporttimingcalenda.data.repository.F1RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        ds: F1RemoteDataSource
    ): F1DataSource

    @Binds
    @Singleton
    abstract fun bindF1Repository(
        repository: F1RepositoryImpl
    ): F1Repository
}
