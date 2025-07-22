package com.example.multiverse_explorer.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.core.ui.components.ErrorState
import com.example.multiverse_explorer.core.ui.components.LoadingState
import com.example.multiverse_explorer.settings.ui.states.SettingsSuccessState


@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val selectedOption by settingsViewModel.dataSource.collectAsState()
    val characterUiState: UiState = settingsViewModel.settingsUiState


    Box(modifier = modifier) {
        when (characterUiState) {
            UiState.Loading -> LoadingState(modifier = modifier)
            UiState.Success -> SettingsSuccessState(
                selectedOption = selectedOption,
                saveDataSource = { dataSource: String ->
                    settingsViewModel.saveDataSource(dataSource = dataSource)
                },
                modifier = modifier
            )

            is UiState.Error -> ErrorState(modifier = modifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        modifier = Modifier.fillMaxSize()
    )
}