package com.aob.inotify.ui.remind_me_view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.aob.inotify.data.model.RemindersData
import com.aob.inotify.database.RemindersDatabaseDao


class RemindMeViewModel(database: RemindersDatabaseDao, application: Application) : AndroidViewModel(application) {


    private val TAG: String = "RemindMeViewModel"

    /**
     * This returns the list of reminders in the database using live data
     * which actively observes changes in the database
     */
    val reminders: LiveData<List<RemindersData>> = database.getAllReminders()
}