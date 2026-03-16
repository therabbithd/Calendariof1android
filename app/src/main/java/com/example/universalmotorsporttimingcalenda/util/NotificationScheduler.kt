package com.example.universalmotorsporttimingcalenda.util

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    fun scheduleTestNotification(title: String, message: String) {
        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putInt("notificationId", (System.currentTimeMillis() % Int.MAX_VALUE).toInt())
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SessionNotificationWorker>()
            .setInputData(data)
            .setInitialDelay(2, TimeUnit.SECONDS) // Short delay for testing
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    // Future implementation for scheduling at specific times
    fun scheduleSessionNotification(title: String, message: String, delayInMillis: Long) {
        if (delayInMillis <= 0) return

        val data = Data.Builder()
            .putString("title", title)
            .putString("message", message)
            .putInt("notificationId", (System.currentTimeMillis() % Int.MAX_VALUE).toInt())
            .build()

        val workRequest = OneTimeWorkRequestBuilder<SessionNotificationWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
