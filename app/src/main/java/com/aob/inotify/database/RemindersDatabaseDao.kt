package com.aob.inotify.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aob.inotify.data.model.RemindersData

@Dao
interface RemindersDatabaseDao {

    /**
     * Insert reminder to the list of reminders in database.
     */
    @Insert
    suspend fun insert(reminder: RemindersData)

    /**
     * This gets all the reminders in the reminder table and orders it using reminderId
     * in descending order.
     */
    @Query("SELECT * FROM reminders_table ORDER BY reminderId DESC")
    fun getAllReminders(): LiveData<List<RemindersData>>

    /**
     * Selects and returns the latest reminder.
     */
    @Query("SELECT * FROM reminders_table ORDER BY reminderId DESC LIMIT 1")
    suspend fun getLatestReminder(): RemindersData?
}