package com.example.multiverse_explorer.characterdetail.ui

import app.cash.turbine.test
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailFromDatabaseUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetCharacterDetailFromNetworkUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesFromDatabaseUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetEpisodesFromNetworkUseCase
import com.example.multiverse_explorer.characterdetail.domain.usecases.GetIdsFromEpisodesUseCase
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
    private lateinit var getCharacterDetailFromDatabaseUseCase: GetCharacterDetailFromDatabaseUseCase

    @MockK
    private lateinit var getCharacterDetailFromNetworkUseCase: GetCharacterDetailFromNetworkUseCase

    @MockK
    private lateinit var getEpisodesFromDatabaseUseCase: GetEpisodesFromDatabaseUseCase

    @MockK
    private lateinit var getEpisodesFromNetworkUseCase: GetEpisodesFromNetworkUseCase

    @MockK
    private lateinit var getIdsFromEpisodesUseCase: GetIdsFromEpisodesUseCase

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
    fun `When the getCharacterDetail succeed from database returns success then the character detail should be updated`() =
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
            val episodeIds = listOf(1, 2, 3)
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

            coEvery { getCharacterDetailFromDatabaseUseCase(characterId = characterId) } returns expectedResult
            coEvery { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) } returns expectedEpisodesResult
            coEvery { getIdsFromEpisodesUseCase(episodes = expectedCharacterDetail.episodes) } returns episodeIds

            characterDetailViewModel =
                CharacterDetailViewModel(
                    getCharacterDetailFromDatabaseUseCase,
                    getCharacterDetailFromNetworkUseCase,
                    getEpisodesFromDatabaseUseCase,
                    getEpisodesFromNetworkUseCase,
                    getIdsFromEpisodesUseCase
                )
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
            coVerify(exactly = 1) { getCharacterDetailFromDatabaseUseCase(characterId = characterId) }
            coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) }
            coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes = expectedCharacterDetail.episodes) }
            assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Success)
        }

    @Test
    fun `When getCharacterDetail fails from database, then network is called and character detail should be updated`() =
        runTest {
            //Given
            val characterId = 1
            val expectedDatabaseResult = ResultApi.Error("Database error")

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
            val expectedNetworkResult = ResultApi.Success(expectedCharacterDetail)

            val episodeIds = listOf(1, 2, 3)
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

            coEvery { getCharacterDetailFromDatabaseUseCase(characterId = characterId) } returns expectedDatabaseResult
            coEvery { getCharacterDetailFromNetworkUseCase(characterId = characterId) } returns expectedNetworkResult
            coEvery { getIdsFromEpisodesUseCase(episodes = expectedCharacterDetail.episodes) } returns episodeIds
            coEvery { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) } returns expectedEpisodesResult


            characterDetailViewModel =
                CharacterDetailViewModel(
                    getCharacterDetailFromDatabaseUseCase,
                    getCharacterDetailFromNetworkUseCase,
                    getEpisodesFromDatabaseUseCase,
                    getEpisodesFromNetworkUseCase,
                    getIdsFromEpisodesUseCase
                )

            //When
            characterDetailViewModel.getCharacterDetail(characterId = characterId)

            //Then
            characterDetailViewModel.characterDetail.test {
                assertEquals(expectedCharacterDetail, awaitItem())
                cancel()
            }
            coVerify(exactly = 1) { getCharacterDetailFromDatabaseUseCase(characterId = characterId) }
            coVerify(exactly = 1) { getCharacterDetailFromNetworkUseCase(characterId = characterId) }
            coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes = expectedCharacterDetail.episodes) }
            coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) }
            assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Success)
        }

    @Test
    fun `When getEpisodes succeed from database then the episodes should be updated`() = runTest {
        //Given
        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )
        val episodeIds = listOf(1, 2, 3)
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

        coEvery { getIdsFromEpisodesUseCase(episodes = episodes) } returns episodeIds
        coEvery { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) } returns expectedEpisodesResult

        characterDetailViewModel =
            CharacterDetailViewModel(
                getCharacterDetailFromDatabaseUseCase,
                getCharacterDetailFromNetworkUseCase,
                getEpisodesFromDatabaseUseCase,
                getEpisodesFromNetworkUseCase,
                getIdsFromEpisodesUseCase
            )

        //When
        characterDetailViewModel.getEpisodes(episodes = episodes)

        //Then
        characterDetailViewModel.episodes.test {
            assertEquals(expectedEpisodes, awaitItem())
            cancel()
        }
        coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes = episodes) }
        coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds)  }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Success)
    }

    @Test
    fun `When getEpisodes fails from database then, then the network is called and the episodes should be updated`() = runTest {
        //Given
        val expectedDatabaseEpisodesResult = ResultApi.Error("Database error")

        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )
        val episodeIds = listOf(1, 2, 3)
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
        val expectedNetworkEpisodesResult = ResultApi.Success(expectedEpisodes)

        coEvery { getIdsFromEpisodesUseCase(episodes = episodes) } returns episodeIds
        coEvery { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) } returns expectedDatabaseEpisodesResult
        coEvery { getEpisodesFromNetworkUseCase(episodeIds = episodeIds) } returns expectedNetworkEpisodesResult

        characterDetailViewModel =
            CharacterDetailViewModel(
                getCharacterDetailFromDatabaseUseCase,
                getCharacterDetailFromNetworkUseCase,
                getEpisodesFromDatabaseUseCase,
                getEpisodesFromNetworkUseCase,
                getIdsFromEpisodesUseCase
            )

        //When
        characterDetailViewModel.getEpisodes(episodes = episodes)

        //Then
        characterDetailViewModel.episodes.test {
            assertEquals(expectedEpisodes, awaitItem())
            cancel()
        }
        coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes = episodes) }
        coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds)  }
        coVerify(exactly = 1) { getEpisodesFromNetworkUseCase(episodeIds = episodeIds)  }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Success)
    }

    @Test
    fun `When both database and network fail then uiState should be Error`() = runTest {
        // Given
        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )
        val episodeIds = listOf(1, 2, 3)
        coEvery { getIdsFromEpisodesUseCase(episodes) } returns episodeIds
        coEvery { getEpisodesFromDatabaseUseCase(episodeIds) } returns ResultApi.Error("Database error")
        coEvery { getEpisodesFromNetworkUseCase(episodeIds) } returns ResultApi.Error("Network error")

        characterDetailViewModel =
            CharacterDetailViewModel(
                getCharacterDetailFromDatabaseUseCase,
                getCharacterDetailFromNetworkUseCase,
                getEpisodesFromDatabaseUseCase,
                getEpisodesFromNetworkUseCase,
                getIdsFromEpisodesUseCase
            )

        // When
        characterDetailViewModel.getEpisodes(episodes)

        // Then
        coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes) }
        coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds) }
        coVerify(exactly = 1) { getEpisodesFromNetworkUseCase(episodeIds) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }


    @Test
    fun `When getEpisodes from network is empty then uiState should be Error`() = runTest {
        //Given
        val expectedDatabaseEpisodesResult = ResultApi.Error("Database error")

        val episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
        )
        val episodeIds = listOf(1, 2, 3)
        val expectedNetworkResult = ResultApi.Success(emptyList<EpisodeDomain>())

        coEvery { getIdsFromEpisodesUseCase(episodes = episodes) } returns episodeIds
        coEvery { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) } returns expectedDatabaseEpisodesResult
        coEvery { getEpisodesFromNetworkUseCase(episodeIds = episodeIds) } returns expectedNetworkResult

        characterDetailViewModel =
            CharacterDetailViewModel(
                getCharacterDetailFromDatabaseUseCase,
                getCharacterDetailFromNetworkUseCase,
                getEpisodesFromDatabaseUseCase,
                getEpisodesFromNetworkUseCase,
                getIdsFromEpisodesUseCase
            )

        //When
        characterDetailViewModel.getEpisodes(episodes)

        //Then
        coVerify(exactly = 1) { getIdsFromEpisodesUseCase(episodes) }
        coVerify(exactly = 1) { getEpisodesFromDatabaseUseCase(episodeIds = episodeIds) }
        coVerify(exactly = 1) { getEpisodesFromNetworkUseCase(episodeIds = episodeIds) }
        assertTrue(characterDetailViewModel.characterDetailUiState is UiState.Error)
    }


}