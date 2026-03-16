package com.example.universalmotorsporttimingcalenda.di

import com.example.universalmotorsporttimingcalenda.data.F1DataSource
import com.example.universalmotorsporttimingcalenda.data.local.F1LocalDataSource
import com.example.universalmotorsporttimingcalenda.data.remote.F1RemoteDataSource
import com.example.universalmotorsporttimingcalenda.data.repository.F1Repository
import com.example.universalmotorsporttimingcalenda.data.repository.F1RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.universalmotorsporttimingcalenda.data.remote.model.CloudinaryConfig
import com.example.universalmotorsporttimingcalenda.BuildConfig
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    @RemoteDataSource
    abstract fun bindRemoteDataSource(
        ds: F1RemoteDataSource
    ): F1DataSource

    @Binds
    @Singleton
    @LocalDataSource
    abstract fun bindLocalDataSource(
        ds: F1LocalDataSource
    ): F1DataSource

    @Binds
    @Singleton
    abstract fun bindF1Repository(
        repository: F1RepositoryImpl
    ): F1Repository

    companion object {
        @Provides
        @Singleton
        fun provideCloudinaryConfig(): CloudinaryConfig {
            return CloudinaryConfig(
                cloudName = BuildConfig.CLOUDINARY_CLOUD_NAME,
                apiKey = BuildConfig.CLOUDINARY_API_KEY,
                apiSecret = BuildConfig.CLOUDINARY_API_SECRET,
                uploadPreset = BuildConfig.CLOUDINARY_UPLOAD_PRESET
            )
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource

