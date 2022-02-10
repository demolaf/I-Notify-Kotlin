package com.aob.inotify.ui.remind_me_view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aob.inotify.database.RemindersDatabaseDao

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the RemindersDatabaseDao and context to the ViewModel.
 */
class RemindMeViewModelFactory(
    private val dataSource: RemindersDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RemindMeViewModel::class.java)) {
            return RemindMeViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}