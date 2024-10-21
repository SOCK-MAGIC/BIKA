package com.shizq.bika.core.database.di

import android.content.Context
import androidx.room.Room
import com.shizq.bika.core.database.BikaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
    @Provides
    @Singleton
    fun providesBikaDatabase(
        @ApplicationContext context: Context,
    ): BikaDatabase = Room.databaseBuilder(
        context,
        BikaDatabase::class.java,
        "bika-database",
    ).build()
}
