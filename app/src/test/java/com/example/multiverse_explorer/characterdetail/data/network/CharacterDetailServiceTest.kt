package com.example.multiverse_explorer.characterdetail.data.network

import com.example.multiverse_explorer.characterdetail.data.network.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.network.model.LocationDetailData
import com.example.multiverse_explorer.characterdetail.data.network.model.OriginDetailData
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


class CharacterDetailServiceTest {

    @MockK
    private lateinit var characterDetailApi: CharacterDetailApi

    private lateinit var characterDetailService: CharacterDetailService

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        characterDetailService = CharacterDetailService(characterDetailApi)
    }

    @Test
    fun `When the api returns success then the service returns success with the characterDetail`() =
        runTest {
            //Given
            val characterId = 1
            val characterDetail = CharacterDetailData(
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
            val expectedResult = retrofit2.Response.success(characterDetail)

            coEvery { characterDetailApi.getCharacterDetail(characterId = characterId) } returns expectedResult


            //When
            val result = characterDetailService.getCharacterDetail(characterId = characterId)

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(characterDetail, result.data)
            coVerify(exactly = 1) { characterDetailApi.getCharacterDetail(characterId = characterId) }
        }


    @Test
    fun `When the api returns error then the service returns error state with the message`() =
        runTest {
            //Given
            val characterId = 1
            val expectedResult =
                retrofit2.Response.error<CharacterDetailData>(500, "Not found".toResponseBody())

            coEvery { characterDetailApi.getCharacterDetail(any()) } returns expectedResult

            //When
            val result = characterDetailService.getCharacterDetail(characterId = characterId)


            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals("Exception: ${expectedResult.message()}", result.message)
            coVerify(exactly = 1) { characterDetailApi.getCharacterDetail(any()) }

        }
}