package com.hcato.hakai.core.di

import android.content.Context
import androidx.room.Room
import com.hcato.hakai.feature.principal.data.datasource.local.HakaiDatabase
import com.hcato.hakai.feature.principal.data.datasource.local.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHakaiDatabase(@ApplicationContext context: Context): HakaiDatabase {
        return Room.databaseBuilder(
            context,
            HakaiDatabase::class.java,
            "hakai_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReminderDao(database: HakaiDatabase): ReminderDao {
        return database.reminderDao()
    }
}