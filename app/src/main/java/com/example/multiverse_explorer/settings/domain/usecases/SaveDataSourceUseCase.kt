package com.example.multiverse_explorer.settings.domain.usecases

import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveDataSourceUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {

    suspend operator fun invoke(dataSource: String) = settingsRepository.saveDataSource(dataSource = dataSource)
}