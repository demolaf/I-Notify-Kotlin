package com.aob.inotify.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aob.inotify.util.NotificationUtil
import java.io.IOException
import java.util.*


class ReminderWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    /**
     * This method is called by work manager once it needs to carry out any scheduled task
     * executed on ReminderWorker
     */
    override fun doWork(): Result {
        // get the name for the notification using inputData from Worker
        val name = inputData.getString("name")
        // get the description for the notification
        val description = inputData.getString("description")
        // create a generator for unique ids, this will make sure notifications don't get replaced
        val id = inputData.getInt(NotificationUtil.NOTIFICATION_ID, Random(System.currentTimeMillis()).nextInt(1000))

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