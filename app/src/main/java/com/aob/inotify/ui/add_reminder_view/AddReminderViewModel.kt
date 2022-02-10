package com.aob.inotify.ui.add_reminder_view

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aob.inotify.data.model.RemindersData
import com.aob.inotify.database.RemindersDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddReminderViewModel(val database: RemindersDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    val TAG: String = "AddReminderViewModel"

    var reminderDate: String? = null
    var reminderTime: String? = null

    /**
     * This inserts the new reminder to the database using the IO thread
     */
    private suspend fun insert(newReminder: RemindersData) {
        // to insert data into a database is an asynchronous task which will require an unknown amount of time
        // so we launch a coroutine on the IO thread and then insert the new "reminder" into the "reminders" database
        withContext(Dispatchers.IO) {
            try {
                database.insert(newReminder)
                Log.d(TAG, "${newReminder.name} Inserted into database")
            } catch (ex: IOException) {
                Log.d(TAG, "Failed to insert into database")
            }

        }
    }

    /**
     * This method contains a coroutine which is used to launch the asynchronous task of saving the new reminder to
     * the database.
     */
    fun insertNewReminder(reminderName: String, reminderDescription: String) {
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newReminder = RemindersData()
            newReminder.apply {
                name = reminderName
                description = reminderDescription
                time = reminderTime.toString()
                date = reminderDate.toString()
            }

            insert(newReminder)
        }
    }

    /**
     * This is used to calculate the time difference from now to the set reminder time.
     */
    fun getDelay(): Long {
        // this is the date and time formatter using the timezone
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.ENGLISH)
        dateFormatter.timeZone = TimeZone.getDefault()

        // get current date
        val currentDate = Date()
        // using the pattern set get the reminder date
        val reminderDate: Date? = dateFormatter.parse("$reminderDate, $reminderTime")

        // get the reminder time in milliseconds
        val reminderTimeMilliseconds: Long? = reminderDate?.time
        // get the current time in milliseconds
        val currentTimeMilliseconds: Long = currentDate.time

        Log.d(TAG, "Reminder milliseconds $reminderTimeMilliseconds CurrentTime milliseconds $currentTimeMilliseconds")

        // try to get the difference between the reminder set time and current time
        // which sets the delay before the notification comes
        return reminderTimeMilliseconds!! - currentTimeMilliseconds
    }

    /**
     * This method is called when the viewmodel gets destroyed
     */
    override fun onCleared() {
        Log.i(TAG, "onCleared")
        super.onCleared()
    }
}


