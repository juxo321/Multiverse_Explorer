package com.example.multiverse_explorer.characters.data

import com.example.multiverse_explorer.characters.data.model.CharacterData
import com.example.multiverse_explorer.characters.data.model.LocationData
import com.example.multiverse_explorer.characters.data.model.OriginData
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
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

class CharactersRepositoryImpTest {
    @MockK
    private lateinit var charactersService: CharactersService
    private lateinit var charactersRepository: CharactersRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersRepository = CharactersRepositoryImp(charactersService)
    }

    @Test
    fun `when service returns success then the repository returns success with mapped characters`() =
        runTest {
            //Given
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
            val expectedCharactersDomain = listOf(
                CharacterDomain(
                    id = 1,
                    name = "Rick",
                    status = "Alive",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    species = "Human",
                    favorite = false,
                ),
                CharacterDomain(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    species = "Human",
                    favorite = false,
                ),
            )
            val expectedResult = ResultApi.Success(charactersData)
            coEvery { charactersService.getCharacters(any()) } returns expectedResult

            //When
            val result = charactersRepository.getCharacters(selectedStatus = "")

            //Then
            assertTrue(result is ResultApi.Success)
            assertEquals(result.data, expectedCharactersDomain)
            coVerify(exactly = 1) { charactersService.getCharacters(selectedStatus = "") }
        }



    @Test
    fun `when service returns error then the repository returns error with a message`() = runTest {
        //Given

        val expectedResult = ResultApi.Error("Network error")
        coEvery { charactersService.getCharacters(any()) } returns  expectedResult

        //When
        val result = charactersRepository.getCharacters("")

        //Then
        assertTrue(result is ResultApi.Error)
        assertEquals(result.message, expectedResult.message)
        coVerify(exactly = 1) { charactersService.getCharacters("") }
    }

}