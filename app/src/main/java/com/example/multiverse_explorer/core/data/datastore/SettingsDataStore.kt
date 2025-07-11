package com.example.multiverse_explorer.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.multiverse_explorer.core.Constants.DataStore.DATA_SOURCE_KEY
import com.example.multiverse_explorer.core.Constants.DataStore.DATA_STORE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(private val context: Context) {

    private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )

    suspend fun saveDataSource(dataSource: String){
        withContext(Dispatchers.IO) {
            context.userPreferencesDataStore.edit { preferences ->
                preferences[DATA_SOURCE_KEY] = dataSource
            }
        }
    }

    suspend fun getDataSource() =
        withContext(Dispatchers.IO){
            context.userPreferencesDataStore.data.map { preferences ->
                preferences[DATA_SOURCE_KEY] ?: "Rest"
            }
        }

}