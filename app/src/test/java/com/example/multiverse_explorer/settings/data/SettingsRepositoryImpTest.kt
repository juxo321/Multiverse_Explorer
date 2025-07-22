package com.example.multiverse_explorer.settings.data

import app.cash.turbine.test
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.data.datastore.SettingsDataStore
import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class SettingsRepositoryImpTest {

    @MockK
    private lateinit var settingsDataStore: SettingsDataStore

    private lateinit var settingsRepository: SettingsRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        settingsRepository = SettingsRepositoryImp(settingsDataStore = settingsDataStore)
    }


    @Test
    fun `When a new data source is saved into the dataStore`() = runTest {

        //Given
        coEvery { settingsDataStore.saveDataSource(any()) } returns Unit

        //When
        settingsRepository.saveDataSource(dataSource = "Rest")

        //Then
        coVerify(exactly = 1) { settingsDataStore.saveDataSource(any()) }
    }

    @Test
    fun `When the data store returns the flow with the data source then the repository returns a mapped flow`() =
        runTest {
            val dataSource = "Rest"
            val expectedResult = flow { emit(dataSource) }
            //Given
            coEvery { settingsDataStore.getDataSource() } returns expectedResult

            //When
            val result = settingsRepository.getDataSource()

            //Then
            result.test {
                val resultDataStore = awaitItem()
                assertTrue(resultDataStore is ResultApi.Success)
                assertEquals(ResultApi.Success("Rest"), resultDataStore)
                awaitComplete()
            }
            coVerify(exactly = 1) { settingsDataStore.getDataSource() }
        }

    @Test
    fun `when the data store returns an exception then the flow returns error`() = runTest {
        //Given
        val expectedResult = Exception("Something went wrong, try again")
        coEvery { settingsDataStore.getDataSource() } returns flow { throw expectedResult }

        //When
        val result = settingsRepository.getDataSource()

        //Then
        result.test {
            val resultApi = awaitItem()
            assertTrue(resultApi is ResultApi.Error)
            assertEquals(expectedResult.message, resultApi.message)
            awaitComplete()
        }
        coVerify(exactly = 1) { settingsDataStore.getDataSource() }
    }

}