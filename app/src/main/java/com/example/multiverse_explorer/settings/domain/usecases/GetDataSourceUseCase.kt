package com.example.multiverse_explorer.settings.domain.usecases

import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataSourceUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(): Flow<ResultApi<String>> =
        settingsRepository.getDataSource()

}