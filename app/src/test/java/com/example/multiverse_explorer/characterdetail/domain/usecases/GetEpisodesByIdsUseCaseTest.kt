package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
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


class GetEpisodesByIdsUseCaseTest {


    @MockK
    private lateinit var characterDetailRepository: CharacterDetailRepository

    private lateinit var getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase


    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        getEpisodesByIdsUseCase = GetEpisodesByIdsUseCase(characterDetailRepository)
    }

    @Test
    fun `When the repository returns success then the use case return Success state with the episodes`() =
        runTest {
            //Given
            val episodesUrls = listOf(
                "https://rickandmortyapi.com/api/episode/1",
                "https://rickandmortyapi.com/api/episode/2",
                "https://rickandmortyapi.com/api/episode/3",
            )
            val episodesIds = listOf(1,2,3)
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

            coEvery { characterDetailRepository.getEpisodes(episodeIds = episodesIds) } returns expectedResult

            //When
            val result = getEpisodesByIdsUseCase(episodes = episodesUrls)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodes, result.data)
            coVerify(exactly = 1) { characterDetailRepository.getEpisodes(any()) }
        }


    @Test
    fun `When the repository returns error then the use case return error state with a message`() =
        runTest {
            //Given
            val expectedResult = ResultApi.Error("Network error")

            coEvery { characterDetailRepository.getEpisodes(any()) } returns expectedResult

            //When
            val result = getEpisodesByIdsUseCase(episodes = emptyList())

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { characterDetailRepository.getEpisodes(any()) }
        }



}