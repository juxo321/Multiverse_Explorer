package com.example.multiverse_explorer.characters.data.network

import com.example.multiverse_explorer.characters.data.network.rest.model.ApiData
import com.example.multiverse_explorer.characters.data.network.rest.model.CharacterData
import com.example.multiverse_explorer.characters.data.network.rest.model.InformationData
import com.example.multiverse_explorer.characters.data.network.rest.model.LocationData
import com.example.multiverse_explorer.characters.data.network.rest.model.OriginData
import com.example.multiverse_explorer.characters.data.network.rest.CharactersApi
import com.example.multiverse_explorer.characters.data.network.rest.CharactersRestDataSource
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


class CharactersRestDataSourceTest {

    @MockK
    private lateinit var charactersApi: CharactersApi
    private lateinit var charactersService: CharactersRestDataSource


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersService = CharactersRestDataSource(charactersApi)
    }

    @Test
    fun `when the api returns success then the service returns success with a list of characters`() =
        runTest {

            //Given
            val informationData = InformationData(
                count = 826,
                pages = 42,
                next = "https://rickandmortyapi.com/api/character?page=2",
                prev = null
            )
            val charactersData = listOf(
                CharacterData(
                    id = 1,
                    name = "Rick",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    origin = OriginData(
                        name = "Earth (C-137)",
                        url = "https://rickandmortyapi.com/api/location/1"
                    ),
                    location = LocationData(
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
                ),
                CharacterData(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    origin = OriginData(
                        name = "unknown",
                        url = ""
                    ),
                    location = LocationData(
                        name = "Citadel of Ricks",
                        url = "https://rickandmortyapi.com/api/location/3"
                    ),
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episode = listOf(
                        "https://rickandmortyapi.com/api/episode/1",
                        "https://rickandmortyapi.com/api/episode/2",
                    ),
                    url = "https://rickandmortyapi.com/api/character/2",
                    created = "2017-11-04T18:50:21.651Z"
                )
            )
            val expectedResult =
                retrofit2.Response.success(ApiData(informationData, charactersData))

            coEvery { charactersApi.getCharacters(any()) } returns expectedResult

            //When
            val result = charactersService.getCharacters(selectedStatus = "")


            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(result.data, charactersData)
            coVerify(exactly = 1) { charactersApi.getCharacters(selectedStatus = "") }
        }

    @Test
    fun `when api returns error then service returns error`() = runTest {
        //Given

        val errorResponse = retrofit2.Response.error<ApiData>(500, "Not found".toResponseBody())
        coEvery { charactersApi.getCharacters(any()) } returns errorResponse

        //When
        val result = charactersService.getCharacters(selectedStatus = "")

        //Then
        assertTrue(result is ResultApi.Error)
        assertEquals("Exception: ${errorResponse.message()}", result.message)
        coVerify(exactly = 1) { charactersApi.getCharacters(selectedStatus = "") }
    }
}
