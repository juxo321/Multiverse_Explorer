package com.example.multiverse_explorer.settings.domain.repository

import coil3.decode.DataSource
import com.example.multiverse_explorer.core.ResultApi
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun saveDataSource(dataSource: String)
    suspend fun getDataSource(): Flow<ResultApi<String>>

}