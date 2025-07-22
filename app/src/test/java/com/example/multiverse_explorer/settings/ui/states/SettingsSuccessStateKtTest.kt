package com.example.multiverse_explorer.settings.ui.states

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.multiverse_explorer.settings.ui.SettingsViewModel
import com.example.multiverse_explorer.ui.theme.Multiverse_ExplorerTheme
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals


@RunWith(RobolectricTestRunner::class)
class SettingsSuccessStateKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun `When the options are displayed for the first time`() {

        //Given
        val selectedOption = "Rest"

        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsSuccessState(
                    selectedOption = selectedOption,
                    saveDataSource = {},
                    modifier = Modifier
                )
            }
        }

        //Then
        composeTestRule.onNodeWithText("Rest").isDisplayed()
        composeTestRule.onNodeWithText("GraphQL API").isDisplayed()
        composeTestRule.onNodeWithText("Select API Source").isDisplayed()

    }

    @Test
    fun `When the rest option is selected`() {
        // Given
        var dataSource = "GraphQL"

        // When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsSuccessState(
                    selectedOption = dataSource,
                    saveDataSource = { newDataSource -> dataSource = newDataSource },
                    modifier = Modifier
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("RestRadioButton").performClick()
        assertEquals("Rest", dataSource)
    }

    @Test
    fun `When the graphql option is selected`() {
        // Given
        var dataSource = "Rest"

        // When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsSuccessState(
                    selectedOption = dataSource,
                    saveDataSource = { newDataSource -> dataSource = newDataSource },
                    modifier = Modifier
                )
            }
        }

        // Then
        composeTestRule.onNodeWithTag("GraphQLRadioButton").performClick()
        assertEquals("GraphQL", dataSource)
    }

}