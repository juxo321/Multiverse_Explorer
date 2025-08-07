package com.example.multiverse_explorer.settings.ui

import app.cash.turbine.test
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.settings.domain.usecases.GetDataSourceUseCase
import com.example.multiverse_explorer.settings.domain.usecases.SaveDataSourceUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @MockK
    private lateinit var saveDataSourceUseCase: SaveDataSourceUseCase

    @MockK
    private lateinit var getDataSourceUseCase: GetDataSourceUseCase

    private lateinit var settingsViewModel: SettingsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `When viewmodel is initialized should get the data source`() = runTest {

        //Given
        val expectedResult = flow { emit(ResultApi.Success("Rest"))  }
        coEvery { getDataSourceUseCase() } returns expectedResult

        //When
        settingsViewModel = SettingsViewModel(
            saveDataSourceUseCase = saveDataSourceUseCase,
            getDataSourceUseCase = getDataSourceUseCase
        )

        //Then
        settingsViewModel.dataSource.test {
            val dataSource = awaitItem()
            assertEquals("Rest", dataSource)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(UiState.Success, settingsViewModel.settingsUiState)
        coVerify(exactly = 1) { getDataSourceUseCase() }
    }

    @Test
    fun `When saveDataSource is called then data source is saved and updated`() = runTest {

        //Given
        val dataSource = "Rest"
        val expectedResult = flow { emit(ResultApi.Success("Rest"))  }
        coEvery { getDataSourceUseCase() } returns expectedResult


        coEvery { saveDataSourceUseCase(dataSource = dataSource) } returns Unit

        settingsViewModel = SettingsViewModel(
            saveDataSourceUseCase = saveDataSourceUseCase,
            getDataSourceUseCase = getDataSourceUseCase
        )

        //When
        settingsViewModel.saveDataSource(dataSource = dataSource)


        //Then
        settingsViewModel.dataSource.test {
            val dataSource = awaitItem()
            assertEquals("Rest", dataSource)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(UiState.Success, settingsViewModel.settingsUiState)
        coVerify(exactly = 1) { getDataSourceUseCase() }
    }


}