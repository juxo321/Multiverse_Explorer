package com.example.multiverse_explorer.characters.ui


import app.cash.turbine.test
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersUseCase
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CharactersViewModelTest {

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var charactersViewModel: CharactersViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        io.mockk.clearAllMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when viewmodel is initialized should load all characters`() = runTest {
        //Given
        val characters = listOf(
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
        val expectedResult = ResultApi.Success(characters)

        coEvery { getCharactersUseCase(selectedStatus = "") } returns expectedResult

        //When
        charactersViewModel = CharactersViewModel(getCharactersUseCase)

        //Then
        assertEquals("All", charactersViewModel.selectedStatus.value)
        assertEquals(UiState.Success, charactersViewModel.charactersUiState)
        charactersViewModel.characters.test {
            assertEquals(characters, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when onStatusSelected should update status and load filtered characters`() = runTest {
        //Given
        val aliveCharacters = listOf(
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
        val expectedResult = ResultApi.Success(aliveCharacters)
        coEvery { getCharactersUseCase(selectedStatus = "") } returns ResultApi.Success(emptyList())

        charactersViewModel = CharactersViewModel(getCharactersUseCase)

        coEvery { getCharactersUseCase(selectedStatus = "Alive") } returns expectedResult

        //When
        charactersViewModel.onStatusSelected(status = "Alive")

        //Then
        assertEquals("Alive", charactersViewModel.selectedStatus.value)
        assertEquals(UiState.Success, charactersViewModel.charactersUiState)
        charactersViewModel.characters.test {
            assertEquals(aliveCharacters, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 2) { getCharactersUseCase(any()) }

    }


    @Test
    fun `when getCharacters returns error then the state should be error`() = runTest {
        //Given
        val expectedResult = ResultApi.Error("Network error")
        coEvery { getCharactersUseCase(selectedStatus = "") } returns expectedResult


        //When
        charactersViewModel = CharactersViewModel(getCharactersUseCase)


        //Then
        assertEquals("All", charactersViewModel.selectedStatus.value)
        assertTrue(charactersViewModel.charactersUiState is UiState.Error)
        val error = charactersViewModel.charactersUiState as UiState.Error
        assertEquals(expectedResult.message, error.message)
    }

    @Test
    fun `when toggleFavorite called then character favourite status is updated`() = runTest {
        //Given
        val characters = listOf(
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

        val expectedResult = ResultApi.Success(characters)

        coEvery { getCharactersUseCase(selectedStatus = "") } returns expectedResult
        charactersViewModel = CharactersViewModel(getCharactersUseCase)

        //When
        charactersViewModel.characters.test {
            awaitItem()

            //When
            charactersViewModel.toggleFavorite(1)

            val updatedCharacters = awaitItem()
            assertEquals(true, updatedCharacters.find { it.id == 1 }?.favorite)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when onSortByNameToggled enabled then should sort characters by name`() = runTest {
        // Given
        val unsortedCharacters = listOf(
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
        coEvery { getCharactersUseCase(selectedStatus = "") } returns ResultApi.Success(unsortedCharacters)
        charactersViewModel = CharactersViewModel(getCharactersUseCase)

        // When
        charactersViewModel.characters.test {
            awaitItem()

            charactersViewModel.onSortByNameToggled(true)

            // Then
            val sortedCharacters = awaitItem()
            assertEquals("Morty Smith", sortedCharacters[0].name)
            assertEquals("Rick", sortedCharacters[1].name)
            cancelAndIgnoreRemainingEvents()
        }
    }




}