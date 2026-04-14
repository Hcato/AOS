package com.hcato.hakai.feature.principal.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val targetTimeInMillis: Long // Cuándo debería sonar
)