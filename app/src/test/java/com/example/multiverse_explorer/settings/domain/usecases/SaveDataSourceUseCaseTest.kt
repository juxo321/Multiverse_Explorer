package com.example.multiverse_explorer.settings.domain.usecases

import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class SaveDataSourceUseCaseTest {

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var saveDataSourceUseCase: SaveDataSourceUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        saveDataSourceUseCase = SaveDataSourceUseCase(settingsRepository = settingsRepository)
    }


    @Test
    fun `When repository returns save the data source`() = runTest {
        val dataSource = "Rest"
        //Given
        coEvery { settingsRepository.saveDataSource(dataSource = dataSource) } returns Unit

        //When
        saveDataSourceUseCase(dataSource = dataSource)

        //Then
        coVerify( exactly = 1) { settingsRepository.saveDataSource(dataSource = dataSource) }
    }

}