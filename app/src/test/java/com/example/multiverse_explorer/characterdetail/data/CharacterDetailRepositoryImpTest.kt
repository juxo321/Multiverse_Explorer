package com.example.multiverse_explorer.characterdetail.data

import com.example.multiverse_explorer.characterdetail.data.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.data.model.LocationDetailData
import com.example.multiverse_explorer.characterdetail.data.model.OriginDetailData
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailService
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeService
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

class CharacterDetailRepositoryImpTest {


    @MockK
    private lateinit var characterDetailService: CharacterDetailService

    @MockK
    private lateinit var episodeService: EpisodeService

    private lateinit var characterDetailRepository: CharacterDetailRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterDetailRepository =
            CharacterDetailRepositoryImp(characterDetailService, episodeService)
    }

    @Test
    fun `When the character detail service returns success then the repository returns success with the character detail`() =
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
            val expectedResult = ResultApi.Success(characterDetailData)

            coEvery { characterDetailService.getCharacterDetail(characterId = characterId) } returns expectedResult

            //When
            val result = characterDetailRepository.getCharacterDetail(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(expectedCharacterDetailDomain, result.data)
            coVerify(exactly = 1) { characterDetailService.getCharacterDetail(characterId = characterId) }
        }

    @Test
    fun `When the character detail service returns error then the repository returns error state with a message`() =
        runTest {
            //Given
            val characterId = 1
            val expectedResult = ResultApi.Error("Network error")

            coEvery { characterDetailService.getCharacterDetail(characterId = characterId) } returns expectedResult

            //When
            val result = characterDetailRepository.getCharacterDetail(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { characterDetailService.getCharacterDetail(characterId = characterId) }
        }

    @Test
    fun `When the episode service returns success then the repository returns success with the episodes`() =
        runTest {
            //Given
            val episodesIds = listOf(1, 2, 3)
            val episodesData = listOf(
                EpisodeData(
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
                EpisodeData(
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
                EpisodeData(
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
            val expectedEpisodesDomain = listOf(
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
            val expectedResult = ResultApi.Success(episodesData)

            coEvery { episodeService.getEpisodes(episodeIds = episodesIds) } returns expectedResult

            //When
            val result = characterDetailRepository.getEpisodes(episodeIds = episodesIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(expectedEpisodesDomain, result.data)
            coVerify(exactly = 1) { episodeService.getEpisodes(episodeIds = episodesIds) }
        }

    @Test
    fun `When the episode service returns error then the repository returns error state with a message`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val expectedResult = ResultApi.Error("Network error")

            coEvery { episodeService.getEpisodes(episodeIds = episodeIds) } returns expectedResult

            //When
            val result = characterDetailRepository.getEpisodes(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { episodeService.getEpisodes(episodeIds = episodeIds) }
        }

}