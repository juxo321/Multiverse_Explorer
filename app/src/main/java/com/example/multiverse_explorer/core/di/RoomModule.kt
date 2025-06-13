package com.example.multiverse_explorer.core.di

import android.content.Context
import androidx.room.Room
import com.example.multiverse_explorer.core.data.database.CharacterDatabase
import com.example.multiverse_explorer.core.Constants.Database.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context, CharacterDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration(false)
        .build()

    @Singleton
    @Provides
    fun provideCharacterDao(database: CharacterDatabase) = database.getCharacterDao()

    @Singleton
    @Provides
    fun provideCharacterDetailDao(database: CharacterDatabase) = database.getCharacterDetailDao()

}