package com.example.multiverse_explorer.characters.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.ui.theme.Multiverse_ExplorerTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CharactersScreenKtTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<CharactersViewModel>(relaxed = true)

    @Test
    fun titleAppIsDisplayed() {

        composeTestRule.setContent {
            TitleApp()
        }

        composeTestRule.onNodeWithTag("icon_app").assertIsDisplayed()
        composeTestRule.onNodeWithTag("title_app").assertIsDisplayed()
    }


    @Test
    fun filterButtonsAreDisplayed() {

        composeTestRule.setContent {
            FilterButtons(
                selectedStatus = "All",
                onStatusSelected = { },
                onSortToggled = { }
            )
        }

        composeTestRule.onNodeWithText("Select status").assertIsDisplayed()
        composeTestRule.onNodeWithText("Name").assertIsDisplayed()
    }

    @Test
    fun filterOptionsAreDisplayedWhenClickFilterButton(){

        composeTestRule.setContent {
            FilterButtons(
                selectedStatus = "All",
                onStatusSelected = { },
                onSortToggled = { }
            )
        }

        composeTestRule.onNodeWithTag("filter_options").performClick()
        composeTestRule.onNodeWithTag("Alive_option").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Dead_option").assertIsDisplayed()
        composeTestRule.onNodeWithTag("Unknown_option").assertIsDisplayed()
    }


    @Test
    fun sortingButtonWorksWhenIsClicked(){
        var isSorted = false

        composeTestRule.setContent {
            FilterButtons(
                selectedStatus = "All",
                onStatusSelected = { },
                onSortToggled = { isSorted = it }
            )
        }

        composeTestRule.onNodeWithTag("sort_button").performClick()

        assert(isSorted)

    }

    @Test
    fun characterScreenShowsLoadingState() {
        every { mockViewModel.charactersUiState } returns UiState.Loading
        every { mockViewModel.characters } returns MutableStateFlow(emptyList())
        every { mockViewModel.selectedStatus } returns mutableStateOf("All")

        composeTestRule.setContent {
            CharactersScreen(
                charactersViewModel = mockViewModel,
                navigateToCharacterDetail = {},
                modifier = Modifier.fillMaxSize()
            )
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun characterScreenShowsErrorState() {
        every { mockViewModel.charactersUiState } returns UiState.Error("Error message")
        every { mockViewModel.characters } returns MutableStateFlow(emptyList())
        every { mockViewModel.selectedStatus } returns mutableStateOf("All")

        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                CharactersScreen(
                    charactersViewModel = mockViewModel,
                    navigateToCharacterDetail = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        composeTestRule.onNodeWithTag("error_state").assertIsDisplayed()
    }

    @Test
    fun characterScreenShowsSuccessState() {
        val testCharacters = listOf(
            CharacterDomain(
                id = 1,
                name = "Rick",
                status = "Alive",
                species = "Human",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            ),
            CharacterDomain(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
            ),
            CharacterDomain(
                id = 3,
                name = "Summer Smith",
                status = "Alive",
                species = "Human",
                image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg"
            )
        )

        every { mockViewModel.charactersUiState } returns UiState.Success
        every { mockViewModel.characters } returns MutableStateFlow(testCharacters)
        every { mockViewModel.selectedStatus } returns mutableStateOf("All")

        composeTestRule.setContent {
            CharactersScreen(
                charactersViewModel = mockViewModel,
                navigateToCharacterDetail = {},
                modifier = Modifier.fillMaxSize()
            )
        }

        composeTestRule.onNodeWithText("Rick").assertIsDisplayed()
        composeTestRule.onNodeWithText("Morty Smith").assertIsDisplayed()
        composeTestRule.onNodeWithText("Summer Smith").assertIsDisplayed()
    }

}