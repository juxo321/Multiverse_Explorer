package com.example.multiverse_explorer.characterdetail.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.characters.ui.CharactersScreen
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.ui.theme.Multiverse_ExplorerTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.Then
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CharacterDetailScreenKtTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<CharacterDetailViewModel>(relaxed = true)


    @Test
    fun `When character screen detail state is Loading`() {
        //Given

        every { mockViewModel.characterDetailUiState } returns UiState.Loading
        every { mockViewModel.characterDetail } returns MutableStateFlow(null)
        every { mockViewModel.episodes } returns MutableStateFlow(emptyList())


        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                CharacterDetailScreen(
                    characterDetailViewModel = mockViewModel,
                    characterId = 1,
                    modifier = Modifier
                )
            }
        }


        //Then
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()


    }


    @Test
    fun `When the character screen detail state is Error`() {
        //Given
        every { mockViewModel.characterDetailUiState } returns UiState.Error("Error message")
        every { mockViewModel.characterDetail } returns MutableStateFlow(null)
        every { mockViewModel.episodes } returns MutableStateFlow(emptyList())


        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                CharacterDetailScreen(
                    characterDetailViewModel = mockViewModel,
                    characterId = 1,
                    modifier = Modifier
                )
            }
        }


        //Then
        composeTestRule.onNodeWithTag("error_state").isDisplayed()
    }

    @Test
    fun `When the character screen state is Success`() {
        //Given

        val characterDetail = CharacterDetailDomain(
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

        val episodes = listOf(
            EpisodeDomain(
                id = 1,
                name = "Pilot"
            ),
            EpisodeDomain(
                id = 2,
                name = "Lawnmower Dog"
            ),
            EpisodeDomain(
                id = 3,
                name = "Anatomy Park"
            )
        )



        every { mockViewModel.characterDetailUiState } returns UiState.Success
        every { mockViewModel.characterDetail } returns MutableStateFlow(characterDetail)
        every { mockViewModel.episodes } returns MutableStateFlow(episodes)


        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                CharacterDetailScreen(
                    characterDetailViewModel = mockViewModel,
                    characterId = 1,
                    modifier = Modifier
                )
            }

        }

        composeTestRule.onNodeWithText("Rick").isDisplayed()
        composeTestRule.onNodeWithText("Alive").isDisplayed()
        composeTestRule.onNodeWithText("Human").isDisplayed()
        composeTestRule.onNodeWithText("Male").isDisplayed()
        composeTestRule.onAllNodesWithText("EP", substring = true).assertCountEquals(3)
    }

}