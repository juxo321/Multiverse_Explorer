package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
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


class GetCharacterDetailFromDatabaseUseCaseTest {

    @MockK
    private lateinit var characterDetailRepository: CharacterDetailRepository

    private lateinit var getCharacterDetailFromDatabaseUseCase: GetCharacterDetailFromDatabaseUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        getCharacterDetailFromDatabaseUseCase = GetCharacterDetailFromDatabaseUseCase(characterDetailRepository)
    }

    @Test
    fun `When the repository returns success then the use case returns Success with the character detail`() = runTest {
        //Given
        val characterId = 1
        val character = CharacterDetailDomain(
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
        val expectedResult = ResultApi.Success(character)
        coEvery { characterDetailRepository.getCharacterDetailFromDatabase( characterId = characterId) } returns expectedResult

        //When
        val result = getCharacterDetailFromDatabaseUseCase(characterId = characterId)

        //Then
        assertTrue(result is ResultApi.Success)
        assertEquals(character, result.data)
        coVerify(exactly = 1) { characterDetailRepository.getCharacterDetailFromDatabase(any()) }
    }


    @Test
    fun `When the repository returns error then the use case return error state with a message`() =
        runTest {
            //Given
            val expectedResult = ResultApi.Error("Database error")

            coEvery { characterDetailRepository.getCharacterDetailFromDatabase(any()) } returns expectedResult

            //When
            val result = getCharacterDetailFromDatabaseUseCase(characterId = 1)

            //Then
            assertTrue(result is ResultApi.Error)
            assertEquals(expectedResult.message, result.message)
            coVerify(exactly = 1) { characterDetailRepository.getCharacterDetailFromDatabase(any()) }
        }

}