package com.example.multiverse_explorer.characters.ui


import app.cash.turbine.test
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.usecases.ClearAllDataUseCase
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersFromNetworkUseCase
import com.example.multiverse_explorer.characters.domain.usecases.GetCharactersUseCase
import com.example.multiverse_explorer.characters.domain.usecases.UpdateFavoriteCharacterUseCase
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.domain.status.UiState
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

    @MockK
    private lateinit var getCharactersFromNetworkUseCase: GetCharactersFromNetworkUseCase

    @MockK
    private lateinit var updateFavoriteCharacterUseCase: UpdateFavoriteCharacterUseCase

    @MockK
    private lateinit var clearAllDataUseCase: ClearAllDataUseCase

    private lateinit var charactersViewModel: CharactersViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        clearAllMocks()
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

        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow { emit(expectedResult) }
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit

        //When
        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )

        //Then
        assertEquals("All", charactersViewModel.selectedStatus.value)
        charactersViewModel.characters.test {
            awaitItem()
            assertEquals(characters, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(UiState.Success, charactersViewModel.charactersUiState)
        coVerify(exactly = 1) { getCharactersUseCase(selectedStatus = "") }
        coVerify(exactly = 1) { getCharactersFromNetworkUseCase(selectedStatus = "") }
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
        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow {
            emit(ResultApi.Success(emptyList())
            )
        }
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit

        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )

        coEvery { getCharactersUseCase(selectedStatus = "Alive") } returns flow {
            emit(expectedResult)
        }

        //When
        charactersViewModel.onStatusSelected(status = "Alive")

        //Then
        assertEquals("Alive", charactersViewModel.selectedStatus.value)
        charactersViewModel.characters.test {
            awaitItem()
            val filteredList = awaitItem()
            assertEquals(aliveCharacters, filteredList)
            cancelAndIgnoreRemainingEvents()
        }
        assertEquals(UiState.Success, charactersViewModel.charactersUiState)
        coVerify(exactly = 1) { getCharactersUseCase(any()) }
        coVerify(exactly = 2) { getCharactersFromNetworkUseCase(any()) }

    }


    @Test
    fun `when getCharacters returns error then the state should be error`() = runTest {
        //Given
        val expectedResult = ResultApi.Error("Database error")
        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow { emit(expectedResult) }
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit


        //When
        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )

        //Then
        assertEquals("All", charactersViewModel.selectedStatus.value)
        charactersViewModel.characters.test {
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }
        val error = charactersViewModel.charactersUiState as UiState.Error
        assertEquals(expectedResult.message, error.message)
        assertTrue(charactersViewModel.charactersUiState is UiState.Error)
        coVerify(exactly = 1) { getCharactersUseCase(any()) }
        coVerify(exactly = 1) { getCharactersFromNetworkUseCase(any()) }
    }

    @Test
    fun `when toggleFavorite called then character favourite status is called`() = runTest {
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
        val updatedCharacters = characters.map {
            if (it.id == 1) it.copy(favorite = true) else it
        }
        val updatedResult = ResultApi.Success(updatedCharacters)

        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow {
            emit(expectedResult)
            emit(updatedResult)
        }
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit
        coEvery { updateFavoriteCharacterUseCase(characterId = 1, isFavorite = any()) } returns 1

        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )

        //When
        charactersViewModel.characters.test {
            val emptyList = awaitItem()
            val charactersLoaded = awaitItem()

            //When
            charactersViewModel.toggleFavorite(1)
            val updatedList = awaitItem()

            assertEquals(true, updatedList.find { it.id == 1 }?.favorite)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { updateFavoriteCharacterUseCase(any(), any()) }
    }

    @Test
    fun `when onSortByNameToggled is called should sort characters`() = runTest {
        // Given
        val unsortedCharacters = listOf(
            CharacterDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                image = "test.jpeg",
                species = "Human",
                favorite = false
            ),
            CharacterDomain(
                id = 2,
                name = "Morty",
                status = "Alive",
                image = "test.jpeg",
                species = "Human",
                favorite = false
            ),
            CharacterDomain(
                id = 3,
                name = "Beth",
                status = "Alive",
                image = "test.jpeg",
                species = "Human",
                favorite = false
            )
        )

        val expectedResult = ResultApi.Success(unsortedCharacters)
        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow { emit(expectedResult) }
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit

        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )

        // When & Then
        charactersViewModel.characters.test {
            val emptyList = awaitItem()
            val charactersLoaded = awaitItem()


            charactersViewModel.onSortByNameToggled()

            val sorted = awaitItem()
            assertEquals("Beth", sorted[0].name)
            assertEquals("Morty", sorted[1].name)
            assertEquals("Rick", sorted[2].name)


            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when clearAllData is called should update ui state and get characters again from network`() = runTest {
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
        coEvery { getCharactersUseCase(selectedStatus = "") } returns flow { emit(expectedResult) }
        coEvery { clearAllDataUseCase() } returns Unit
        coEvery { getCharactersFromNetworkUseCase(any()) } returns Unit

        charactersViewModel = CharactersViewModel(
            getCharactersFromNetworkUseCase = getCharactersFromNetworkUseCase,
            getCharactersUseCase = getCharactersUseCase,
            updateFavoriteCharacterUseCase = updateFavoriteCharacterUseCase,
            clearAllDataUseCase = clearAllDataUseCase
        )


        charactersViewModel.characters.test {
            val emptyList = awaitItem()
            val charactersLoaded = awaitItem()

            charactersViewModel.clearAllData()
            assertEquals(true, charactersViewModel.isRefreshing.value)
            assertEquals(UiState.Loading, charactersViewModel.charactersUiState)

            cancelAndIgnoreRemainingEvents()
        }

        //Then
        coVerify(exactly = 1) { clearAllDataUseCase() }
        coVerify(exactly = 2) { getCharactersFromNetworkUseCase(selectedStatus = any()) }



    }


}