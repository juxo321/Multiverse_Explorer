package com.example.multiverse_explorer.characters.data

import app.cash.turbine.test
import com.example.multiverse_explorer.characters.data.database.CharacterWithRelations
import com.example.multiverse_explorer.characters.data.database.dao.CharacterDao
import com.example.multiverse_explorer.characters.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.characters.data.database.entities.LocationEntity
import com.example.multiverse_explorer.characters.data.database.entities.OriginEntity
import com.example.multiverse_explorer.characters.data.mappers.toDomain
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.data.network.model.CharacterData
import com.example.multiverse_explorer.characters.data.network.model.LocationData
import com.example.multiverse_explorer.characters.data.network.model.OriginData
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
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

class CharactersRepositoryImpTest {
    @MockK
    private lateinit var charactersService: CharactersService

    @MockK
    private lateinit var characterDao: CharacterDao
    private lateinit var charactersRepository: CharactersRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersRepository = CharactersRepositoryImp(
            charactersService = charactersService,
            characterDao = characterDao
        )
    }


    @Test
    fun `when service returns success then the repository save the characters into the database`() =
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
//            val expectedCharactersDomain = listOf(
//                CharacterDomain(
//                    id = 1,
//                    name = "Rick",
//                    status = "Alive",
//                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
//                    species = "Human",
//                    favorite = false,
//                ),
//                CharacterDomain(
//                    id = 2,
//                    name = "Morty Smith",
//                    status = "Alive",
//                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
//                    species = "Human",
//                    favorite = false,
//                ),
//            )
            val expectedResult = ResultApi.Success(charactersData)
            coEvery { charactersService.getCharacters(any()) } returns expectedResult
            coEvery { characterDao.getFavoriteCharacters() } returns emptyList()
            coEvery { characterDao.insertOrigins(any()) } returns listOf(1L, 2L)
            coEvery { characterDao.insertLocations(any()) } returns listOf(1L, 2L)
            coEvery { characterDao.insertCharacters(any()) } returns listOf(1L, 2L)

            //When
            charactersRepository.getCharactersFromNetwork(selectedStatus = "")

            //Then
            coVerify(exactly = 1) { charactersService.getCharacters(selectedStatus = "") }
            coVerify(exactly = 1) { characterDao.insertOrigins(any()) }
            coVerify(exactly = 1) { characterDao.insertLocations(any()) }
            coVerify(exactly = 1) { characterDao.insertCharacters(any()) }
        }


    @Test
    fun `when service returns error then the repository doesn't save to database`() = runTest {
        //Given

        val expectedResult = ResultApi.Error("Network error")
        coEvery { charactersService.getCharacters(any()) } returns expectedResult

        //When
        charactersRepository.getCharactersFromNetwork("")

        //Then
        coVerify(exactly = 1) { charactersService.getCharacters("") }
        coVerify(exactly = 0) { characterDao.insertCharacters(any()) }
    }

    @Test
    fun `when dao returns characters then the flow returns success`() = runTest {
        //Given
        val expectedCharacters = listOf(
            CharacterWithRelations(
                character = CharacterEntity(
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
                    isFavorite = false
                ),
                origin = OriginEntity(
                    id = 1,
                    name = "Earth (C-137)",
                    url = "https://rickandmortyapi.com/api/location/1"
                ),
                location = LocationEntity(
                    id = 1,
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                )
            ),

            CharacterWithRelations(
                character = CharacterEntity(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    originId = 2,
                    locationId = 2,
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episode = listOf(
                        "https://rickandmortyapi.com/api/episode/1",
                        "https://rickandmortyapi.com/api/episode/2"
                    ),
                    url = "https://rickandmortyapi.com/api/character/2",
                    created = "2017-11-04T18:50:21.651Z",
                    isFavorite = false
                ),
                origin = OriginEntity(
                    id = 2,
                    name = "unknown",
                    url = ""
                ),
                location = LocationEntity(
                    id = 2,
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                )
            )
        )

        val expectedResult = flow { emit(expectedCharacters) }

        coEvery { characterDao.getCharactersByStatus(any()) } returns expectedResult

        //When
        val result = charactersRepository.getCharactersFromDatabase("")

        //Then
        result.test {
            val resultApi = awaitItem()
            assertTrue(resultApi is ResultApi.Success)
            assertEquals(expectedCharacters.map { it.toDomain() }, resultApi.data)
            awaitComplete()
        }
        coVerify(exactly = 1) { characterDao.getCharactersByStatus(any()) }
    }

    @Test
    fun `when the dao returns an exception then the flow returns error`() = runTest {
        //Given
        val expectedResult = Exception("Database error")
        coEvery { characterDao.getCharactersByStatus(any()) } returns flow { throw expectedResult }

        //When
        val result = charactersRepository.getCharactersFromDatabase("")

        //Then
        result.test {
            val resultApi = awaitItem()
            assertTrue(resultApi is ResultApi.Error)
            assertEquals(expectedResult.message, resultApi.message)
            awaitComplete()
        }
        coVerify(exactly = 1) { characterDao.getCharactersByStatus(any()) }

    }


    @Test
    fun `when the new characters are saved into the database and persist the favorite characters`() = runTest {
        //Given
        val expectedResult = listOf(
            CharacterWithRelations(
                character = CharacterEntity(
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
                ),
                origin = OriginEntity(
                    id = 1,
                    name = "Earth (C-137)",
                    url = "https://rickandmortyapi.com/api/location/1"
                ),
                location = LocationEntity(
                    id = 1,
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                )
            ),

            CharacterWithRelations(
                character = CharacterEntity(
                    id = 2,
                    name = "Morty Smith",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    originId = 2,
                    locationId = 2,
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    episode = listOf(
                        "https://rickandmortyapi.com/api/episode/1",
                        "https://rickandmortyapi.com/api/episode/2"
                    ),
                    url = "https://rickandmortyapi.com/api/character/2",
                    created = "2017-11-04T18:50:21.651Z",
                    isFavorite = true
                ),
                origin = OriginEntity(
                    id = 2,
                    name = "unknown",
                    url = ""
                ),
                location = LocationEntity(
                    id = 2,
                    name = "Citadel of Ricks",
                    url = "https://rickandmortyapi.com/api/location/3"
                )
            )
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

        coEvery { characterDao.getFavoriteCharacters() } returns expectedResult
        coEvery { characterDao.insertOrigins(any()) } returns listOf(1L, 2L)
        coEvery { characterDao.insertLocations(any()) } returns listOf(1L, 2L)
        coEvery { characterDao.insertCharacters(any()) } returns listOf(1L, 2L)


        //When
        charactersRepository.saveCharactersToDatabase(charactersData = charactersData)

        //Then
        coVerify(exactly = 1) { characterDao.getFavoriteCharacters() }
        coVerify(exactly = 1) { characterDao.insertOrigins(any()) }
        coVerify(exactly = 1) { characterDao.insertLocations(any()) }
        coVerify(exactly = 1) { characterDao.insertCharacters(any()) }
    }

    @Test
    fun `when the character's favorite status is updated`() = runTest {
        //Given
        coEvery { characterDao.updateFavoriteCharacter(characterId = 1, isFavorite = false) } returns 1

        //When
        val result = charactersRepository.updateFavoriteCharacter(characterId = 1, isFavorite = false)

        //Then
        coVerify( exactly = 1) { characterDao.updateFavoriteCharacter(characterId = 1, isFavorite = false) }
        assertEquals(1, result)

    }

}