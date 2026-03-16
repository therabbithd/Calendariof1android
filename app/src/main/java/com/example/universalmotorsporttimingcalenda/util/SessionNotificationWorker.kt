package com.example.universalmotorsporttimingcalenda.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SessionNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val title = inputData.getString("title") ?: "F1 Session"
        val message = inputData.getString("message") ?: "Session is starting soon!"
        val notificationId = inputData.getInt("notificationId", 1)

        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()
        notificationHelper.showNotification(title, message, notificationId)

        return Result.success()
    }
}
