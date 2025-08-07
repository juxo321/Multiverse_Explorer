package com.example.multiverse_explorer.settings.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
class SettingsScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mockk<SettingsViewModel>(relaxed = true)

    @Test
    fun `When character screen detail state is Loading`() {
        //Given

        every { mockViewModel.settingsUiState } returns UiState.Loading
        every { mockViewModel.dataSource } returns MutableStateFlow("Rest")


        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsScreen(
                    settingsViewModel = mockViewModel,
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
        every { mockViewModel.settingsUiState } returns UiState.Error("Error message")
        every { mockViewModel.dataSource } returns MutableStateFlow("Rest")

        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsScreen(
                    settingsViewModel = mockViewModel,
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

        val dataSource = "Rest"

        every { mockViewModel.settingsUiState } returns UiState.Success
        every { mockViewModel.dataSource } returns MutableStateFlow(dataSource)


        //When
        composeTestRule.setContent {
            Multiverse_ExplorerTheme {
                SettingsScreen(
                    settingsViewModel = mockViewModel,
                    modifier = Modifier
                )
            }

        }

        composeTestRule.onNodeWithText("Rest").isDisplayed()
        composeTestRule.onNodeWithText("GraphQL API").isDisplayed()
        composeTestRule.onNodeWithText("Select API Source").isDisplayed()

    }
}