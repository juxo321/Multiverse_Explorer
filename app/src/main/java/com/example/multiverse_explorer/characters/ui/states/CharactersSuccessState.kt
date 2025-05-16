package com.example.multiverse_explorer.characters.ui.states

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.multiverse_explorer.R
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain


@Composable
fun CharactersSuccessState(
    characters: List<CharacterDomain>,
    navigateToCharacterDetail: (characterId: Int) -> Unit,
    toggleFavorite: (characterId: Int) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        items(characters) {
            CharacterItem(
                character = it,
                navigateToCharacterDetail = navigateToCharacterDetail,
                toggleFavorite = toggleFavorite
            )
        }
    }
}

@Composable
fun CharacterItem(
    character: CharacterDomain,
    navigateToCharacterDetail: (characterId: Int) -> Unit,
    toggleFavorite: (characterId: Int) -> Unit,
) {

    ElevatedCard(
        onClick = { navigateToCharacterDetail(character.id) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = "${character.name}_image",
                error = painterResource(R.drawable.error_image),
                onError = { Log.i("Error image", it.toString()) },
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            CharacterInfo(
                character = character,
                modifier = Modifier.weight(4f)
            )
            IconToggleButton(
                checked = character.favorite,
                onCheckedChange = { toggleFavorite(character.id) },
                colors = IconToggleButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.LightGray,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray,
                    checkedContainerColor = Color.Transparent,
                    checkedContentColor = Color.Red
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("favorite_toggle")
            ) {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Formality icon"
                )
            }

        }
    }
}


@Composable
fun CharacterInfo(
    character: CharacterDomain, modifier:
    Modifier = Modifier)
{
    Column(
        modifier = modifier.padding(12.dp)
    ) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 30.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "Species: ${character.species}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "Status: ${character.status}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = when (character.status) {
                "Alive" -> MaterialTheme.colorScheme.primary
                "Dead" -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CharactersSuccessStatePreview() {
    val sampleCharacters = getSampleCharacters()
    CharactersSuccessState(
        characters = sampleCharacters,
        navigateToCharacterDetail = { },
        modifier = Modifier.fillMaxSize(),
        toggleFavorite = {},
    )
}

fun getSampleCharacters(): List<CharacterDomain> = listOf(
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