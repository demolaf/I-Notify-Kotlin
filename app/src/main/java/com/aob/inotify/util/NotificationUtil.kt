package com.aob.inotify.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aob.inotify.ui.remind_me_view.RemindMeViewFragment

class NotificationUtil(var name: String, var description: String) {

    companion object {
        const val NOTIFICATION_CHANNEL = "inotify_notificaion_channel"
        const val NOTIFICATION_ID = "inotify_notification_id"
        const val NOTIFICATION_NAME = "inotify_notification_name"
    }

    /**
     * This method is used to create our notification and pass in the reminders
     * name and description
     */
    fun showNotification(context: Context, id: Int) {
        // create an intent
        val intent = Intent(context, RemindMeViewFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setContentTitle(name)
            .setContentText(description)
            .setSmallIcon(com.google.android.material.R.drawable.ic_clock_black_24dp)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // set notification priority to max
        notification.priority = NotificationCompat.PRIORITY_MAX


        val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

        channel.apply {
            enableLights(true)
            enableVibration(true)
            lightColor = Color.RED
            setSound(ringtoneManager, audioAttributes)
        }

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(id, notification.build())
        Log.d("ReminderWorker", "Reminder name $name, description $description")
    }
}