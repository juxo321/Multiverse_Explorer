package com.example.multiverse_explorer.characterdetail.domain.usecases

import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class GetIdsFromEpisodesUseCaseTest {

    private lateinit var getIdsFromEpisodesUseCase: GetIdsFromEpisodesUseCase

    @Before
    fun setUp() {
        getIdsFromEpisodesUseCase = GetIdsFromEpisodesUseCase()
    }

    @Test
    fun `When the use case receives a list of episodes then returns the episodes ids`() {
        // Given
        val episodesUseCase = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3"
        )
        val expectedIds = listOf(1, 2, 3)

        // When
        val result = getIdsFromEpisodesUseCase(episodes = episodesUseCase)

        // Then
        assertEquals(expectedIds, result)
    }

    @Test
    fun `When the use case receives an empty list then returns an empty list`() {
        // Given
        val episodesUseCase = emptyList<String>()
        val expectedIds = emptyList<Int>()

        // When
        val result = getIdsFromEpisodesUseCase(episodes = episodesUseCase)

        // Then
        assertEquals(expectedIds, result)
    }

}