package com.aob.inotify.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Reminder Data model
 */
@Entity(tableName = "reminders_table")
data class RemindersData(
    @PrimaryKey(autoGenerate = true)
    val reminderId: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "description")
    var description: String = "",
    @ColumnInfo(name = "time")
    var time: String = "",
    @ColumnInfo(name = "date")
    var date: String = ""
)
