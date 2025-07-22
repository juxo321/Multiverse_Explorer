package com.example.multiverse_explorer.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {

    @Provides
    fun providesContext(@ApplicationContext context: Context) = context
}