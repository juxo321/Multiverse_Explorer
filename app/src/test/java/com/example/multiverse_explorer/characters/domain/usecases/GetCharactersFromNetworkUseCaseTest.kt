package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test


class GetCharactersFromNetworkUseCaseTest {

    @MockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharactersFromNetworkUseCase: GetCharactersFromNetworkUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        getCharactersFromNetworkUseCase = GetCharactersFromNetworkUseCase(charactersRepository)
    }

    @Test
    fun `Repository getCharactersFromNetwork with correct status`() = runTest {
        // Given
        val status = "Alive"
        coEvery { charactersRepository.getCharactersFromNetwork(any()) } returns Unit

        // When
        getCharactersFromNetworkUseCase(status)

        // Then
        coVerify(exactly = 1) { charactersRepository.getCharactersFromNetwork(status) }
    }

}