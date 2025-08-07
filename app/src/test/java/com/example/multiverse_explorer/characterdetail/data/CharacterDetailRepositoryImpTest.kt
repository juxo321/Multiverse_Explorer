package com.example.multiverse_explorer.characterdetail.data

import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.database.entities.EpisodeEntity
import com.example.multiverse_explorer.characterdetail.data.mappers.toDomain
import com.example.multiverse_explorer.characterdetail.data.mappers.toEntity
import com.example.multiverse_explorer.characterdetail.data.network.graphql.CharacterDetailGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.graphql.EpisodeGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.LocationDetailData
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.OriginDetailData
import com.example.multiverse_explorer.characterdetail.data.network.rest.CharacterDetailRestDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.EpisodeRestDataSource
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.data.datastore.SettingsDataStore
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

class CharacterDetailRepositoryImpTest {

    @MockK
    private lateinit var settingsDataStore: SettingsDataStore

    @MockK
    private lateinit var characterDetailRestDataSource: CharacterDetailRestDataSource

    @MockK
    private lateinit var characterDetailGraphQLDataSource: CharacterDetailGraphQLDataSource

    @MockK
    private lateinit var episodeRestDataSource: EpisodeRestDataSource

    @MockK
    private lateinit var episodeGraphQLDataSource: EpisodeGraphQLDataSource

    @MockK
    private lateinit var characterDetailDao: CharacterDetailDao

    private lateinit var characterDetailRepository: CharacterDetailRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterDetailRepository = CharacterDetailRepositoryImp(
            settingsDataStore = settingsDataStore,
            characterDetailRestDataSource = characterDetailRestDataSource,
            characterDetailGraphQLDataSource = characterDetailGraphQLDataSource,
            episodeRestDataSource = episodeRestDataSource,
            episodeGraphQLDataSource = episodeGraphQLDataSource,
            characterDetailDao = characterDetailDao
        )
    }

    @Test
    fun `When the database returns the character detail then the repository returns the character detail`() =
        runTest {
            //Given
            val characterId = 1
            val characterEntity = CharacterEntity(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                originId = 1,
                locationId = 1,
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2"
                ),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z",
                isFavorite = true
            )
            coEvery { characterDetailDao.getCharacterDetail(characterId = characterId) } returns characterEntity

            //When
            val result =
                characterDetailRepository.getCharacterDetailFromDatabase(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(characterEntity.toDomain(), result.data)
            coVerify(exactly = 1) { characterDetailDao.getCharacterDetail(characterId = characterId) }
        }


    @Test
    fun `When the database returns null then the repository returns error`() = runTest {
        //Given
        val characterId = 1

        coEvery { characterDetailDao.getCharacterDetail(characterId = characterId) } returns null

        //When
        val result =
            characterDetailRepository.getCharacterDetailFromDatabase(characterId = characterId)

        //Then
        assertTrue(result is ResultApi.Error)
        assertEquals("Character doesn't exist", result.message)
        coVerify(exactly = 1) { characterDetailDao.getCharacterDetail(characterId = characterId) }
    }

    @Test
    fun `When REST character detail datasource returns success for character detail then the repository returns success`() =
        runTest {
            //Given
            val characterId = 1
            val characterDetailData = CharacterDetailData(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = OriginDetailData(
                    name = "Earth (C-137)",
                    url = "https://rickandmortyapi.com/api/location/1"
                ),
                location = LocationDetailData(
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                ),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                ),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            )
            val expectedCharacterDetailDomain = CharacterDetailDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episodes = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                ),
            )
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("Rest") }
            coEvery { characterDetailRestDataSource.getCharacterDetail(characterId = characterId) } returns ResultApi.Success(characterDetailData)

            //When
            val result = characterDetailRepository.getCharacterDetailFromNetwork(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(expectedCharacterDetailDomain, result.data)
            coVerify(exactly = 1) { characterDetailRestDataSource.getCharacterDetail(characterId = characterId) }
            coVerify(exactly = 0) { characterDetailGraphQLDataSource.getCharacterDetail(any()) }
        }

    @Test
    fun `When GraphQL character detail datasource returns success for character detail then the repository returns success`() =
        runTest {
            //Given
            val characterId = 1
            val characterDetailData = CharacterDetailData(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = OriginDetailData(
                    name = "Earth (C-137)",
                    url = "https://rickandmortyapi.com/api/location/1"
                ),
                location = LocationDetailData(
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                ),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                ),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            )
            val expectedCharacterDetailDomain = CharacterDetailDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                gender = "Male",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episodes = listOf(
                    "https://rickandmortyapi.com/api/episode/1",
                    "https://rickandmortyapi.com/api/episode/2",
                ),
            )
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("GraphQL") }
            coEvery { characterDetailGraphQLDataSource.getCharacterDetail(characterId = characterId) } returns ResultApi.Success(characterDetailData)

            //When
            val result = characterDetailRepository.getCharacterDetailFromNetwork(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(expectedCharacterDetailDomain, result.data)
            coVerify(exactly = 0) { characterDetailRestDataSource.getCharacterDetail(any()) }
            coVerify(exactly = 1) { characterDetailGraphQLDataSource.getCharacterDetail(characterId = characterId) }
        }

    @Test
    fun `When character detail service returns error then the repository returns error state with a message`() =
        runTest {
            //Given
            val characterId = 1
            val expectedResult = ResultApi.Error("Network error")
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("Rest") }
            coEvery { characterDetailRestDataSource.getCharacterDetail(characterId = characterId) } returns expectedResult

            //When
            val result = characterDetailRepository.getCharacterDetailFromNetwork(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { characterDetailRestDataSource.getCharacterDetail(characterId = characterId) }
            coVerify(exactly = 0) { characterDetailGraphQLDataSource.getCharacterDetail(any()) }
        }


    @Test
    fun `When the database returns the episodes then the repository returns the episodes`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val episodesEntity = listOf(
                EpisodeEntity(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2",
                        "https://rickandmortyapi.com/api/character/35",
                        "https://rickandmortyapi.com/api/character/38",
                        "https://rickandmortyapi.com/api/character/62",
                        "https://rickandmortyapi.com/api/character/92",
                        "https://rickandmortyapi.com/api/character/127",
                        "https://rickandmortyapi.com/api/character/144",
                        "https://rickandmortyapi.com/api/character/158",
                        "https://rickandmortyapi.com/api/character/175",
                        "https://rickandmortyapi.com/api/character/179",
                        "https://rickandmortyapi.com/api/character/181",
                        "https://rickandmortyapi.com/api/character/239",
                        "https://rickandmortyapi.com/api/character/249",
                        "https://rickandmortyapi.com/api/character/271",
                        "https://rickandmortyapi.com/api/character/338",
                        "https://rickandmortyapi.com/api/character/394",
                        "https://rickandmortyapi.com/api/character/395",
                        "https://rickandmortyapi.com/api/character/435"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/1",
                    created = "2017-11-10T12:56:33.798Z"
                ),
                EpisodeEntity(
                    id = 2,
                    name = "Lawnmower Dog",
                    airDate = "December 9, 2013",
                    episode = "S01E02",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2",
                        "https://rickandmortyapi.com/api/character/38",
                        "https://rickandmortyapi.com/api/character/46",
                        "https://rickandmortyapi.com/api/character/63",
                        "https://rickandmortyapi.com/api/character/80",
                        "https://rickandmortyapi.com/api/character/175",
                        "https://rickandmortyapi.com/api/character/221",
                        "https://rickandmortyapi.com/api/character/239",
                        "https://rickandmortyapi.com/api/character/246",
                        "https://rickandmortyapi.com/api/character/304",
                        "https://rickandmortyapi.com/api/character/305",
                        "https://rickandmortyapi.com/api/character/306",
                        "https://rickandmortyapi.com/api/character/329",
                        "https://rickandmortyapi.com/api/character/338",
                        "https://rickandmortyapi.com/api/character/396",
                        "https://rickandmortyapi.com/api/character/397",
                        "https://rickandmortyapi.com/api/character/398",
                        "https://rickandmortyapi.com/api/character/405"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/2",
                    created = "2017-11-10T12:56:33.916Z"
                ),
                EpisodeEntity(
                    id = 3,
                    name = "Anatomy Park",
                    airDate = "December 16, 2013",
                    episode = "S01E03",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2",
                        "https://rickandmortyapi.com/api/character/12",
                        "https://rickandmortyapi.com/api/character/17",
                        "https://rickandmortyapi.com/api/character/38",
                        "https://rickandmortyapi.com/api/character/45",
                        "https://rickandmortyapi.com/api/character/96",
                        "https://rickandmortyapi.com/api/character/97",
                        "https://rickandmortyapi.com/api/character/98",
                        "https://rickandmortyapi.com/api/character/99",
                        "https://rickandmortyapi.com/api/character/100",
                        "https://rickandmortyapi.com/api/character/101",
                        "https://rickandmortyapi.com/api/character/108",
                        "https://rickandmortyapi.com/api/character/112",
                        "https://rickandmortyapi.com/api/character/114",
                        "https://rickandmortyapi.com/api/character/169",
                        "https://rickandmortyapi.com/api/character/175",
                        "https://rickandmortyapi.com/api/character/186",
                        "https://rickandmortyapi.com/api/character/201",
                        "https://rickandmortyapi.com/api/character/268",
                        "https://rickandmortyapi.com/api/character/300",
                        "https://rickandmortyapi.com/api/character/302",
                        "https://rickandmortyapi.com/api/character/338",
                        "https://rickandmortyapi.com/api/character/356"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/3",
                    created = "2017-11-10T12:56:34.022Z"
                ),
            )
            coEvery { characterDetailDao.getEpisodes(episodeIds = episodeIds) } returns episodesEntity

            //When
            val result = characterDetailRepository.getEpisodesFromDatabase(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodesEntity.map { it.toDomain() }, result.data)
            coVerify(exactly = 1) { characterDetailDao.getEpisodes(episodeIds = episodeIds) }
        }


    @Test
    fun `When the database returns empty list of episodes then the repository returns error`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val characterId = 1

            coEvery { characterDetailDao.getEpisodes(episodeIds = episodeIds) } returns emptyList()

            //When
            val result = characterDetailRepository.getEpisodesFromDatabase(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals("Episodes don't exist", result.message)
            coVerify(exactly = 1) { characterDetailDao.getEpisodes(episodeIds = episodeIds) }
        }

    @Test
    fun `When REST episode datasource returns success for episodes then the repository returns success`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val episodesData = listOf(
                EpisodeData(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2",
                        "https://rickandmortyapi.com/api/character/35"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/1",
                    created = "2017-11-10T12:56:33.798Z"
                ),
                EpisodeData(
                    id = 2,
                    name = "Lawnmower Dog",
                    airDate = "December 9, 2013",
                    episode = "S01E02",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/2",
                    created = "2017-11-10T12:56:33.916Z"
                ),
                EpisodeData(
                    id = 3,
                    name = "Anatomy Park",
                    airDate = "December 16, 2013",
                    episode = "S01E03",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/3",
                    created = "2017-11-10T12:56:34.022Z"
                )
            )
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("Rest") }
            coEvery { episodeRestDataSource.getEpisodes(episodeIds = episodeIds) } returns ResultApi.Success(episodesData)
            coEvery { characterDetailDao.insertEpisodes(episodes = episodesData.map { it.toEntity() }) } returns Unit

            //When
            val result = characterDetailRepository.getEpisodesFromNetwork(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodesData.map { it.toDomain() }, result.data)
            coVerify(exactly = 1) { episodeRestDataSource.getEpisodes(episodeIds = episodeIds) }
            coVerify(exactly = 0) { episodeGraphQLDataSource.getEpisodes(any()) }
            coVerify(exactly = 1) { characterDetailDao.insertEpisodes(episodes = episodesData.map { it.toEntity() }) }
        }

    @Test
    fun `When GraphQL episode datasource returns success for episodes then the repository returns success`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val episodesData = listOf(
                EpisodeData(
                    id = 1,
                    name = "Pilot",
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2",
                        "https://rickandmortyapi.com/api/character/35"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/1",
                    created = "2017-11-10T12:56:33.798Z"
                ),
                EpisodeData(
                    id = 2,
                    name = "Lawnmower Dog",
                    airDate = "December 9, 2013",
                    episode = "S01E02",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/2",
                    created = "2017-11-10T12:56:33.916Z"
                ),
                EpisodeData(
                    id = 3,
                    name = "Anatomy Park",
                    airDate = "December 16, 2013",
                    episode = "S01E03",
                    characters = listOf(
                        "https://rickandmortyapi.com/api/character/1",
                        "https://rickandmortyapi.com/api/character/2"
                    ),
                    url = "https://rickandmortyapi.com/api/episode/3",
                    created = "2017-11-10T12:56:34.022Z"
                )
            )
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("GraphQL") }
            coEvery { episodeGraphQLDataSource.getEpisodes(episodeIds = episodeIds) } returns ResultApi.Success(episodesData)
            coEvery { characterDetailDao.insertEpisodes(episodes = episodesData.map { it.toEntity() }) } returns Unit

            //When
            val result = characterDetailRepository.getEpisodesFromNetwork(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodesData.map { it.toDomain() }, result.data)
            coVerify(exactly = 0) { episodeRestDataSource.getEpisodes(any()) }
            coVerify(exactly = 1) { episodeGraphQLDataSource.getEpisodes(episodeIds = episodeIds) }
            coVerify(exactly = 1) { characterDetailDao.insertEpisodes(episodes = episodesData.map { it.toEntity() }) }
        }

    @Test
    fun `When episode data source returns error then the repository returns error state with a message`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val expectedResult = ResultApi.Error("Network error")
            coEvery { settingsDataStore.getDataSource() } returns flow { emit("Rest") }
            coEvery { episodeRestDataSource.getEpisodes(episodeIds = episodeIds) } returns expectedResult

            //When
            val result = characterDetailRepository.getEpisodesFromNetwork(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { episodeRestDataSource.getEpisodes(episodeIds = episodeIds) }
            coVerify(exactly = 0) { episodeGraphQLDataSource.getEpisodes(any()) }
        }

}