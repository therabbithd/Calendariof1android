package com.example.universalmotorsporttimingcalenda.di

import com.example.universalmotorsporttimingcalenda.data.remote.F1Api
import com.example.universalmotorsporttimingcalenda.data.remote.F1RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideF1Api(): F1Api {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(F1Api::class.java)
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        api: F1Api,
        scope: CoroutineScope
    ): F1RemoteDataSource = F1RemoteDataSource(api, scope)
}
