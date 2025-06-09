package com.example.multiverse_explorer.characterdetail.ui

import app.cash.turbine.test
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesByIdsUseCase
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    @MockK
    private lateinit var getCharacterDetailUseCase: GetCharacterDetailUseCase

    @MockK
    private lateinit var getEpisodesByIdsUseCase: GetEpisodesByIdsUseCase

    private lateinit var characterDetailViewModel: CharacterDetailViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        io.mockk.clearAllMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the getCharacterDetail returns success then the character detail should be updated`() =
        runTest {
            //Given

            val characterId = 1
            val expectedCharacterDetail = CharacterDetailDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episodes = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                    "https://rickandmortyapi.com/api/episode/3",
                ),
            )
            val expectedResult = ResultApi.Success(expectedCharacterDetail)
            val expectedEpisodes = listOf(
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
            val expectedEpisodesResult = ResultApi.Success(expectedEpisodes)

            coEvery { getCharacterDetailUseCase(characterId = characterId) } returns expectedResult
            coEvery { getEpisodesByIdsUseCase(episodes = expectedCharacterDetail.episodes) } returns expectedEpisodesResult

            characterDetailViewModel =
                CharacterDetailViewModel(getCharacterDetailUseCase, getEpisodesByIdsUseCase)
            //When
            characterDetailViewModel.getCharacterDetail(characterId = characterId)

            //Then
            characterDetailViewModel.characterDetail.test {
                assertEquals(expectedCharacterDetail, awaitItem())
                cancel()
            }
            characterDetailViewModel.episodes.test {
                assertEquals(expectedEpisodes, awaitItem())
                cancel()
            }
            assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Success)
            coVerify(exactly = 1) { getCharacterDetailUseCase(characterId = characterId) }
            coVerify(exactly = 1) { getEpisodesByIdsUseCase(episodes = expectedCharacterDetail.episodes) }
        }

    @Test
    fun `When getCharacterDetail fails then uiState should be Error`() = runTest {
        //Given
        val characterId = 1
        val expectedResult = ResultApi.Error("Network error")

        coEvery { getCharacterDetailUseCase(characterId = characterId) } returns expectedResult

        characterDetailViewModel =
            CharacterDetailViewModel(getCharacterDetailUseCase, getEpisodesByIdsUseCase)

        //When
        characterDetailViewModel.getCharacterDetail(characterId = characterId)

        //Then
        coVerify(exactly = 1) { getCharacterDetailUseCase(characterId = characterId) }
        coVerify(exactly = 0) { getEpisodesByIdsUseCase(any()) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }

    @Test
    fun `When character is not found then uiState should be Error`() = runTest {
        //Given
        val characterId = 1
        val expectedResult = ResultApi.Success(null)

        coEvery { getCharacterDetailUseCase(characterId = characterId) } returns expectedResult

        characterDetailViewModel =
            CharacterDetailViewModel(getCharacterDetailUseCase, getEpisodesByIdsUseCase)

        //When
        characterDetailViewModel.getCharacterDetail(characterId = characterId)

        //Then
        coVerify(exactly = 1) { getCharacterDetailUseCase(characterId = characterId) }
        coVerify(exactly = 0) { getEpisodesByIdsUseCase(any()) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }

    @Test
    fun `When the getEpisodes fails then should be error state`() = runTest {
        //Given
        val expectedResult = ResultApi.Error("Network error")
        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )


        coEvery { getEpisodesByIdsUseCase(any()) } returns expectedResult

        characterDetailViewModel =
            CharacterDetailViewModel(getCharacterDetailUseCase, getEpisodesByIdsUseCase)

        //When
        characterDetailViewModel.getEpisodes(episodes = episodes)

        //Then
        coVerify(exactly = 1) { getEpisodesByIdsUseCase(any()) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }

    @Test
    fun `When episodes list is empty then uiState should be Error`() = runTest {
        //Given
        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )
        val expectedResult = ResultApi.Success(emptyList<EpisodeDomain>())

        coEvery { getEpisodesByIdsUseCase(episodes = episodes) } returns expectedResult

        characterDetailViewModel = CharacterDetailViewModel(getCharacterDetailUseCase, getEpisodesByIdsUseCase)

        //When
        characterDetailViewModel.getEpisodes(episodes)

        //Then
        coVerify(exactly = 1) { getEpisodesByIdsUseCase(episodes = episodes) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }


}