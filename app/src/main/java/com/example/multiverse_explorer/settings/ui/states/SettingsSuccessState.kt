package com.example.multiverse_explorer.settings.ui.states

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multiverse_explorer.R
import com.example.multiverse_explorer.settings.ui.SettingsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSuccessState(
    selectedOption: String,
    saveDataSource: (String) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(
            start = 10.dp
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                bottom = 8.dp
            )
        ) {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(30.dp),
                tooltip = {
                    PlainTooltip { Text(stringResource(R.string.settings_screen_switch_description)) }
                },
                state = rememberTooltipState()
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(R.string.settings_screen_icon_info_description)
                    )
                }
            }
            Text(
                text = stringResource(R.string.settings_screen_select_label),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = selectedOption == "Rest",
                onClick = { saveDataSource("Rest") }
            )
            Text(
                text = stringResource(R.string.settings_screen_rest_option),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = selectedOption == "GraphQL",
                onClick = { saveDataSource("GraphQL") }
            )
            Text(
                text = stringResource(R.string.settings_screen_graph_option),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsSuccessStatePreview() {
    SettingsSuccessState(
        selectedOption = "Rest",
        saveDataSource = {},
        modifier = Modifier
    )
}