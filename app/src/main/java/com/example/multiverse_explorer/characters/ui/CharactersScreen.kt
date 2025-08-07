package com.example.multiverse_explorer.characters.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.multiverse_explorer.R
import com.example.multiverse_explorer.characters.ui.states.CharactersSuccessState
import com.example.multiverse_explorer.core.Constants
import com.example.multiverse_explorer.core.domain.status.UiState
import com.example.multiverse_explorer.core.ui.components.ErrorState
import com.example.multiverse_explorer.core.ui.components.LoadingState
import com.example.multiverse_explorer.core.ui.components.Menu


@Composable
fun CharactersScreen(
    charactersViewModel: CharactersViewModel = hiltViewModel(),
    navigateToCharacterDetail: (characterId: Int) -> Unit,
    navigateToSettings: () -> Unit,
    windowSize: WindowSizeClass,
    modifier: Modifier
) {

    val charactersUiState = charactersViewModel.charactersUiState
    val characters by charactersViewModel.characters.collectAsState(emptyList())
    val selectedStatus by charactersViewModel.selectedStatus.collectAsState()
    val isNameSorted by charactersViewModel.isNameSorted
    val selectedCharacterId by charactersViewModel.selectedCharacterId

    Column(modifier = modifier) {
        HeaderApp(
            onClearData = {
                charactersViewModel.clearAllData()
            },
            navigateToSettings = navigateToSettings,
            selectedStatus = selectedStatus,
            isNameSorted = isNameSorted,
            onStatusSelected = {
                charactersViewModel.onStatusSelected(it)
            },
            onSortToggled = {
                charactersViewModel.onSortByNameToggled()
            },
            windowSize = windowSize
        )
        Box(modifier = Modifier.weight(1f)) {
            when (charactersUiState) {
                UiState.Loading -> LoadingState(modifier = Modifier.fillMaxSize())
                UiState.Success -> CharactersSuccessState(
                    characters = characters,
                    selectedCharacterId = selectedCharacterId,
                    onNavigateToDetail = navigateToCharacterDetail,
                    onSelectCharacter = { characterId ->
                        charactersViewModel.onCharacterSelected(characterId)
                    },
                    onToggleFavorite = { characterId: Int ->
                        charactersViewModel.toggleFavorite(characterId = characterId)
                    },
                    windowSize = windowSize,
                    modifier = Modifier.fillMaxSize()
                )

                is UiState.Error -> ErrorState(modifier = Modifier.fillMaxSize())
            }
        }
    }

}

@Composable
fun HeaderApp(
    onClearData: () -> Unit,
    navigateToSettings: () -> Unit,
    selectedStatus: String,
    isNameSorted: Boolean,
    onStatusSelected: (status: String) -> Unit,
    onSortToggled: () -> Unit,
    windowSize: WindowSizeClass
) {
    when (windowSize.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                TitleApp(
                    onClearData = onClearData,
                    navigateToSettings = navigateToSettings,
                    modifier = Modifier.fillMaxWidth()
                )
                FilterButtons(
                    selectedStatus = selectedStatus,
                    isNameSorted = isNameSorted,
                    onStatusSelected = onStatusSelected,
                    onSortToggled = onSortToggled
                )
            }
        }

        WindowWidthSizeClass.EXPANDED -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                FilterButtons(
                    selectedStatus = selectedStatus,
                    isNameSorted = isNameSorted,
                    onStatusSelected = onStatusSelected,
                    onSortToggled = onSortToggled
                )
                TitleApp(
                    onClearData = onClearData,
                    navigateToSettings = navigateToSettings
                )
            }
        }

        else -> {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                TitleApp(
                    onClearData = onClearData,
                    navigateToSettings = navigateToSettings,
                    modifier = Modifier.fillMaxWidth()
                )
                FilterButtons(
                    selectedStatus = selectedStatus,
                    isNameSorted = isNameSorted,
                    onStatusSelected = onStatusSelected,
                    onSortToggled = onSortToggled
                )
            }
        }
    }
}


@Composable
fun TitleApp(
    onClearData: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.icon_app),
            contentDescription = "icon_app",
            modifier = Modifier
                .size(40.dp)
                .testTag("icon_app")
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 4.dp)
                .testTag("title_app")
        )
        Spacer(modifier = Modifier.weight(1f))
        Menu(
            onClearData = onClearData,
            navigateToSettings = navigateToSettings,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtons(
    selectedStatus: String,
    isNameSorted: Boolean,
    onStatusSelected: (status: String) -> Unit,
    onSortToggled: () -> Unit,
    modifier: Modifier = Modifier
) {

    val statusOptions = Constants.Filter.STATUS_OPTIONS
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .testTag("filter_options")
        ) {
            TextField(
                readOnly = true,
                value = selectedStatus,
                onValueChange = {},
                label = { Text(stringResource(R.string.characters_filter_status_label)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                statusOptions.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onStatusSelected(it)
                            expanded = false
                        },
                        modifier = Modifier.testTag("${it}_option")
                    )
                }
            }
        }
        FilledIconButton(
            onClick = {
                onSortToggled()
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(start = 8.dp)
                .width(100.dp)
                .testTag("sort_button")
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.characters_sort_name_button))
                Icon(
                    imageVector = if (isNameSorted) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = stringResource(R.string.characters_sort_name_description)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CharactersScreenPreview() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderApp(
                onClearData = { },
                navigateToSettings = { },
                selectedStatus = "All",
                isNameSorted = false,
                onStatusSelected = { },
                onSortToggled = { },
                windowSize = currentWindowAdaptiveInfo().windowSizeClass
            )
            Box(modifier = Modifier.weight(1f)) {
                LoadingState(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
