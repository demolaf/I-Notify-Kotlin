package com.aob.inotify.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aob.inotify.ui.remind_me_view.RemindMeViewFragment

class NotificationUtil(var name: String, var description: String) {

    companion object {
        const val NOTIFICATION_CHANNEL = "inotify_notification_channel"
        const val NOTIFICATION_ID = "inotify_notification_id"
        const val NOTIFICATION_NAME = "inotify_notification_name"
    }

    /**
     * This method is used to create our notification and pass in the reminders
     * name and description
     */
    fun showNotification(context: Context, id: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            // use the default notification ringer on device
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // we add audio attributes
            // first we set usage; "why" you are playing a sound, what is this sound used for.
            // second we set the content type; "what" you are playing.
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            // We add a few extra things like notification lights on devices that support an indicator for notifications
            // We also enable vibration
            channel.apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC;
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                setSound(ringtoneManager, audioAttributes)
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Create a pending intent
        // A pending intent is an intent that will be done at a later time when an action is carried out
        // Here this action is when the user taps the notification.
        val intent = Intent(context, RemindMeViewFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Create notification
        // Here we set our notification title, text, icon, a pending intent that carries out some action
        // when the notification is tapped.
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setContentTitle(name)
            .setContentText(description)
            .setSmallIcon(com.google.android.material.R.drawable.ic_clock_black_24dp)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)

        // Post a notification to be shown in the status bar. If a notification with the same id has already been
        // posted by your application and has not yet been canceled, it will be replaced by the updated information.
        notificationManager.notify(id, notification.build())
        Log.d("ReminderWorker", "Reminder name $name, description $description")
    }
}