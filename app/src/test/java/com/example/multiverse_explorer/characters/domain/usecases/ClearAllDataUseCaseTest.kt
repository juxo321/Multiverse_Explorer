package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


class ClearAllDataUseCaseTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var clearAllDataUseCase: ClearAllDataUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        clearAllDataUseCase = ClearAllDataUseCase(charactersRepository = charactersRepository)
    }

    @Test
    fun `When repository clear all data in the database`() = runTest {
        //Given
        coEvery { charactersRepository.clearAllData() } returns Unit

        //When
        clearAllDataUseCase()

        //Then
        coVerify( exactly = 1) { charactersRepository.clearAllData() }
    }

}