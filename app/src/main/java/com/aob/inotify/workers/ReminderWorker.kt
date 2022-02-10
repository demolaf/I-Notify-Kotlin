package com.aob.inotify.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aob.inotify.util.NotificationUtil
import java.io.IOException


class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * This method is called by work manager once it needs to carry out any scheduled task
     * executed on ReminderWorker
     */
    override fun doWork(): Result {
        val name = inputData.getString("name")
        val description = inputData.getString("description")
        val id = inputData.getInt(NotificationUtil.NOTIFICATION_ID, 0)

        // try to do work if successful return success else return failure
        return try {
            // do the work of sending notification
            val notificationUtil = NotificationUtil(name.toString(), description.toString())
            notificationUtil.showNotification(applicationContext, id)
            Result.success()
        } catch (e: IOException) {
            Result.failure()
        }

    }
}