package com.example.multiverse_explorer.characters.domain.usecases

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


class GetCharactersUseCaseTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCharactersUseCase = GetCharactersUseCase(charactersRepository)
    }

    @Test
    fun `when repository returns success then use case returns characters`() = runTest {
        //Given
        val characters = listOf(
            CharacterDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                image = "url",
                species = "Human",
                favorite = false,
            ),
            CharacterDomain(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                image = "url",
                species = "Human",
                favorite = false,
            ),
        )
        val expectedResult = ResultApi.Success(characters)
        coEvery { charactersRepository.getCharacters(any()) } returns expectedResult


        //When
        val result = getCharactersUseCase(selectedStatus = "")

        //Then
        assertTrue(result is ResultApi.Success)
        assertEquals(characters, result.data)
        coVerify(exactly = 1) { charactersRepository.getCharacters(selectedStatus = "") }

    }

    @Test
    fun `when repository returns error then the use case returns error`() = runTest {
        //Given
        val expectedResult = ResultApi.Error("Network error")

        coEvery { charactersRepository.getCharacters(any()) } returns expectedResult

        //When
        val result = getCharactersUseCase(selectedStatus = "")

        //Then
        assertTrue(result is ResultApi.Error)
        assertEquals(expectedResult.message, result.message)
        coVerify( exactly = 1 ) { charactersRepository.getCharacters(selectedStatus = "") }
    }


}