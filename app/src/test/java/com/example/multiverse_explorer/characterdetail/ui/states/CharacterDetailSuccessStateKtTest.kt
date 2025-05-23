package com.example.multiverse_explorer.characterdetail.ui.states

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.ui.theme.Multiverse_ExplorerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CharacterDetailSuccessStateKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val characterDetail = CharacterDetailDomain(
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

    private val episodes = listOf(
        EpisodeDomain(
            id = 1, name = "Pilot"
        ), EpisodeDomain(
            id = 2, name = "Lawnmower Dog"
        ), EpisodeDomain(
            id = 3, name = "Anatomy Park"
        )
    )

    @Test
    fun `When character detail is displayed then image is shown`() {

        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                ImageDetail(
                    characterDetail = characterDetail,
                    modifier = Modifier
                )
            }
        }


        composeTestRule
            .onNode(hasContentDescription("${characterDetail.name}_image"))
            .assertExists()

    }

    @Test
    fun `When character detail is displayed then the characters info is shown`() {


        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                CharacterDetailInfo(
                    characterDetail = characterDetail,
                    modifier = Modifier
                )
            }

        }

        composeTestRule.onNodeWithText("Rick").assertIsDisplayed()
        composeTestRule.onNodeWithText("Species: Human").assertIsDisplayed()
        composeTestRule.onNodeWithText("Status: Alive").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gender: Male").assertIsDisplayed()


    }

    @Test
    fun `When character detail is displayed then the episodes are shown`() {

        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                EpisodesDetail(
                    episodes = episodes,
                    modifier = Modifier
                )
            }
        }


        composeTestRule.onNodeWithText("Pilot")
        composeTestRule.onNodeWithText("Lawnmower Dog")
        composeTestRule.onNodeWithText("Anatomy Park")
        composeTestRule.onAllNodesWithText(text = "EP", substring = true)
            .assertCountEquals(3)
    }

}