package com.example.multiverse_explorer.settings.domain.usecases

import app.cash.turbine.test
import com.example.multiverse_explorer.core.ResultApi
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


class GetDataSourceUseCaseTest {

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var getDataSourceUseCase: GetDataSourceUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        getDataSourceUseCase = GetDataSourceUseCase(settingsRepository = settingsRepository)
    }

    @Test
    fun `When repository returns success then the use case returns the data source`() = runTest {

        val expectedResult = flow { emit(ResultApi.Success("Rest"))  }

        //Given
        coEvery { settingsRepository.getDataSource() } returns expectedResult

        //When
        val result = getDataSourceUseCase()

        //Then
        result.test {
            val dataSource = awaitItem()
            assertTrue(dataSource is ResultApi.Success)
            assertEquals(ResultApi.Success("Rest"), dataSource)
            awaitComplete()
        }
        coVerify( exactly = 1) { settingsRepository.getDataSource() }
    }

    @Test
    fun `when repository returns error then the use case returns error`() = runTest {
        //Given
        val expectedResult = ResultApi.Error("datastore error")

        coEvery { settingsRepository.getDataSource() } returns flow { emit(expectedResult) }

        //When
        val result = getDataSourceUseCase()

        //Then
        result.test {
            val error = awaitItem()
            assertTrue(error is ResultApi.Error)
            assertEquals(expectedResult.message, error.message)
            awaitComplete()
        }
        coVerify(exactly = 1) { settingsRepository.getDataSource() }
    }
}
