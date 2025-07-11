package com.example.multiverse_explorer.settings.data

import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.data.datastore.SettingsDataStore
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImp @Inject constructor(private val settingsDataStore: SettingsDataStore) :
    SettingsRepository {
    override suspend fun saveDataSource(dataSource: String) {
        settingsDataStore.saveDataSource(dataSource = dataSource)
    }

    override suspend fun getDataSource(): Flow<ResultApi<String>> =
        settingsDataStore.getDataSource().map<String, ResultApi<String>> {
            ResultApi.Success(it)
        }.catch { e ->
            emit(ResultApi.Error(e.message ?: "Something went wrong, try again!"))
        }
}