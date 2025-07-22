package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.ResultApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetEpisodesFromDatabaseUseCaseTest {
    @MockK
    private lateinit var characterDetailRepository: CharacterDetailRepository

    private lateinit var getEpisodesFromDatabaseUseCase: GetEpisodesFromDatabaseUseCase


    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        getEpisodesFromDatabaseUseCase = GetEpisodesFromDatabaseUseCase(characterDetailRepository)
    }

    @Test
    fun `When the repository returns success then the use case return Success state with the episodes`() =
        runTest {
            //Given
            val episodeIds = listOf(1,2,3)
            val episodes = listOf(
                EpisodeDomain(
                    id = 1,
                    name = "Pilot"
                ),
                EpisodeDomain(
                    id = 2,
                    name = "Lawnmower Dog"
                ),
                EpisodeDomain(
                    id = 3,
                    name = "Anatomy Park"
                )
            )
            val expectedResult = ResultApi.Success(episodes)

            coEvery { characterDetailRepository.getEpisodesFromDatabase(episodeIds = episodeIds) } returns expectedResult

            //When
            val result = getEpisodesFromDatabaseUseCase(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodes, result.data)
            coVerify(exactly = 1) { characterDetailRepository.getEpisodesFromDatabase(any()) }
        }


    @Test
    fun `When the repository returns error then the use case return error state with a message`() =
        runTest {
            //Given
            val expectedResult = ResultApi.Error("Database error")

            coEvery { characterDetailRepository.getEpisodesFromDatabase(any()) } returns expectedResult

            //When
            val result = getEpisodesFromDatabaseUseCase(episodeIds = emptyList())

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { characterDetailRepository.getEpisodesFromDatabase(any()) }
        }


}

