package com.example.multiverse_explorer.characters.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.multiverse_explorer.R
import com.example.multiverse_explorer.characters.domain.state.CharactersUiState
import com.example.multiverse_explorer.characters.ui.states.CharactersSuccessState
import com.example.multiverse_explorer.core.ui.components.ErrorState
import com.example.multiverse_explorer.core.ui.components.LoadingState


@Composable
fun CharactersScreen(
    charactersViewModel: CharactersViewModel = hiltViewModel(),
    navigateToCharacterDetail: (characterId: Int) -> Unit,
    modifier: Modifier
) {

    val charactersUiState = charactersViewModel.charactersUiState
    val characters by charactersViewModel.characters.collectAsState(emptyList())
    val selectedStatus by charactersViewModel.selectedStatus

    Column(modifier = modifier) {
        TitleApp()
        FilterButtons(
            selectedStatus = selectedStatus,
            onStatusSelected = {
                charactersViewModel.onStatusSelected(it)
            },
            onSortToggled = {
                charactersViewModel.onSortByNameToggled(it)
            }
        )
        Box {
            when (charactersUiState) {
                CharactersUiState.Loading -> LoadingState(modifier = modifier)
                CharactersUiState.Success -> CharactersSuccessState(
                    characters = characters,
                    navigateToCharacterDetail = navigateToCharacterDetail,
                    toggleFavorite = { characterId: Int ->
                        charactersViewModel.toggleFavorite(characterId = characterId)
                    },
                    modifier = modifier
                )

                is CharactersUiState.Error -> ErrorState(modifier = modifier)
            }
        }
    }

}


@Composable
fun TitleApp() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.icon_app),
            contentDescription = "app_icon",
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = stringResource(R.string.title_app),
            style = MaterialTheme.typography.headlineLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButtons(
    selectedStatus: String,
    onStatusSelected: (status: String) -> Unit,
    onSortToggled: (Boolean) -> Unit
) {

    val statusOptions = listOf("All", "Alive", "Dead", "Unknown")
    var expanded by remember { mutableStateOf(false) }
    var isNameSorted by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(3f)
        ) {
            TextField(
                readOnly = true,
                value = selectedStatus,
                onValueChange = {},
                label = { Text("Select status") },
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
                        }
                    )
                }
            }
        }
        FilledIconButton(
            onClick = {
                isNameSorted = !isNameSorted
                onSortToggled(isNameSorted)
            },
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name")
                Icon(
                    imageVector = if (isNameSorted) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = "Sort by name"
                )
            }
        }
    }
}
