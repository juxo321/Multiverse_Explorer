package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class UpdateFavoriteCharacterUseCaseTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var updateFavoriteCharacterUseCase: UpdateFavoriteCharacterUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        updateFavoriteCharacterUseCase = UpdateFavoriteCharacterUseCase(charactersRepository)
    }


    @Test
    fun `When the repository updates character's favorite status then return rows modified`() = runTest{
        //Given

        coEvery { charactersRepository.updateFavoriteCharacter(characterId = 1, isFavorite = false) } returns 1

        //When
        val result = updateFavoriteCharacterUseCase(characterId = 1, isFavorite = false)

        //Then
        coVerify(exactly = 1){ charactersRepository.updateFavoriteCharacter(characterId = 1, isFavorite = false) }
        assertEquals(1, result)
    }

    @Test
    fun `When the repository fails to update character's favorite status`() = runTest{
        //Given

        coEvery { charactersRepository.updateFavoriteCharacter(characterId = 1, isFavorite = false) } returns 0

        //When
        val result = updateFavoriteCharacterUseCase(characterId = 1, isFavorite = false)

        //Then
        coVerify(exactly = 1){ charactersRepository.updateFavoriteCharacter(characterId = 1, isFavorite = false) }
        assertEquals(0, result)
    }
}