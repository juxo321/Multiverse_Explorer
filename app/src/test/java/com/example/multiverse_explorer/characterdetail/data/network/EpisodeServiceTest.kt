package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.network.model.EpisodeData
import com.example.multiverse_explorer.core.ResultApi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EpisodeServiceTest {

    @MockK
    private lateinit var episodeApi: EpisodeApi

    private lateinit var episodeService: EpisodeService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        episodeService = EpisodeService(episodeApi)
    }

    @Test
    fun `When the api returns success then the service returns success with the episodes`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val episodes = listOf(
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
            val expectedResult = retrofit2.Response.success(episodes)

            coEvery { episodeApi.getEpisodes(episodeIds = episodeIds) } returns expectedResult


            //When
            val result = episodeService.getEpisodes(episodeIds = episodeIds)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(episodes, result.data)
            coVerify(exactly = 1) { episodeApi.getEpisodes(episodeIds = episodeIds) }
        }


    @Test
    fun `When the api returns error then the service returns error state with the message`() =
        runTest {
            //Given
            val episodeIds = listOf(1, 2, 3)
            val expectedResult =
                retrofit2.Response.error<List<EpisodeData>>(500, "Not found".toResponseBody())

            coEvery { episodeApi.getEpisodes(any()) } returns expectedResult

            //When
            val result = episodeService.getEpisodes(episodeIds = episodeIds)


            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals("Exception: ${expectedResult.message()}", result.message)
            coVerify(exactly = 1) { episodeApi.getEpisodes(any()) }

        }

}