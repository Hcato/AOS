package com.hcato.hakai.feature.principal.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlin.jvm.JvmSuppressWildcards

@Dao
interface ReminderDao {

    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @JvmSuppressWildcards
    @Query("SELECT EXISTS(SELECT 1 FROM reminders WHERE videoId = :videoId)")
    suspend fun isReminderSet(videoId: String): Boolean
}