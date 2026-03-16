package com.example.universalmotorsporttimingcalenda.di

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp
import com.example.universalmotorsporttimingcalenda.data.remote.model.CloudinaryConfig
import javax.inject.Inject

@HiltAndroidApp
class UmtcApplication : Application() {

    @Inject
    lateinit var cloudinaryConfig: CloudinaryConfig

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Cloudinary
        val config: Map<String, String?> = mapOf(
            "cloud_name" to cloudinaryConfig.cloudName,
            "api_key" to cloudinaryConfig.apiKey,
            "api_secret" to cloudinaryConfig.apiSecret
        )
        MediaManager.init(this, config)
    }
}
