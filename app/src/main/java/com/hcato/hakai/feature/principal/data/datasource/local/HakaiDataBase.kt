package com.hcato.hakai.feature.principal.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReminderEntity::class], version = 1, exportSchema = false)
abstract class HakaiDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}