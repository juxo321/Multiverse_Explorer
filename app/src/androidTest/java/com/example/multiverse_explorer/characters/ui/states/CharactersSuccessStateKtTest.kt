package com.example.multiverse_explorer.characters.ui.states

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import org.junit.Rule
import org.junit.Test

class CharactersSuccessStateKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun showAllCharacters() {

        val characters = listOf(
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

        composeTestRule.setContent {
            CharactersSuccessState(
                characters = characters,
                navigateToCharacterDetail = {},
                toggleFavorite = {},
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Rick").assertExists()
        composeTestRule.onNodeWithText("Morty Smith").assertExists()
        composeTestRule.onNodeWithText("Summer Smith").assertExists()

    }


    @Test
    fun characterItemShowsCorrectInformation(){


        val character = CharacterDomain(
            id = 1,
            name = "Rick",
            status = "Alive",
            species = "Human",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        composeTestRule.setContent {
            CharacterItem(
                character = character,
                navigateToCharacterDetail = {},
                toggleFavorite = { }
            )
        }

        composeTestRule.onNodeWithText("Rick").assertExists()
        composeTestRule.onNodeWithText("Species: ${character.species}").assertExists()
        composeTestRule.onNodeWithText( "Status: ${character.status}").assertExists()
        composeTestRule.onNodeWithContentDescription("${character.name}_image").assertExists().isDisplayed()
    }


    @Test
    fun characterItemClick(){
        var navigationId = 0
        val character = CharacterDomain(
            id = 1,
            name = "Rick",
            status = "Alive",
            species = "Human",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        composeTestRule.setContent {
            CharacterItem(
                character = character,
                navigateToCharacterDetail = {  navigationId = it  },
                toggleFavorite = { }
            )
        }


        composeTestRule.onNodeWithText("Rick").performClick()
        assert(navigationId == character.id)
    }

    @Test
    fun characterItemTogglesFavorite(){
        var favorite = false

        val character = CharacterDomain(
            id = 1,
            name = "Rick",
            status = "Alive",
            species = "Human",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        )

        composeTestRule.setContent {
            CharacterItem(
                character = character,
                navigateToCharacterDetail = {},
                toggleFavorite = { favorite = !favorite }
            )
        }

        composeTestRule.onNodeWithTag("favorite_toggle").performClick()
        assert(favorite)
    }

}