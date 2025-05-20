package com.example.multiverse_explorer.characterdetail.ui.states

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.multiverse_explorer.R
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain
import com.example.multiverse_explorer.core.Constants.Filter.ALIVE
import com.example.multiverse_explorer.core.Constants.Filter.DEAD


@Composable
fun CharacterDetailSuccessState(
    characterDetail: CharacterDetailDomain,
    episodes: List<EpisodeDomain>,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            ImageDetail(
                characterDetail = characterDetail,
                modifier = Modifier
                    .weight(1f)
            )
            CharacterDetailInfo(
                characterDetail = characterDetail,
                modifier = Modifier.weight(1f)
            )
        }
        EpisodesDetail(
            episodes = episodes,
            modifier = modifier.weight(3f)
        )

    }

}

@Composable
fun ImageDetail(
    characterDetail: CharacterDetailDomain,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        AsyncImage(
            model = characterDetail.image,
            contentDescription = "${characterDetail.name}_image",
            error = painterResource(R.drawable.error_image),
            onError = { Log.i("Error image", it.toString()) },
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
fun CharacterDetailInfo(
    characterDetail: CharacterDetailDomain,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        Text(
            text = characterDetail.name,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 30.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "Species: ${characterDetail.species}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "Status: ${characterDetail.status}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = when (characterDetail.status) {
                ALIVE -> MaterialTheme.colorScheme.primary
                DEAD -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "Gender: ${characterDetail.gender}",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}


@Composable
fun EpisodesDetail(
    episodes: List<EpisodeDomain>,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.padding(24.dp)) {
        items(episodes) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "EP ${it.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(end = 16.dp)
                )
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(0.8f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailSuccessStatePreview() {
    val sampleCharacterDetail = CharacterDetailDomain(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episodes = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3",
            "https://rickandmortyapi.com/api/episode/4",
        )
    )

    val sampleEpisodes = listOf(
        EpisodeDomain(
            id = 1,
            name = "Pilot",
        ),
        EpisodeDomain(
            id = 2,
            name = "Lawnmower Dog",
        ),
        EpisodeDomain(
            id = 3,
            name = "Anatomy Park",
        ),
        EpisodeDomain(
            id = 4,
            name = "M. Night Shaym-Aliens!",
        )
    )

    CharacterDetailSuccessState(
        characterDetail = sampleCharacterDetail,
        episodes = sampleEpisodes,
        modifier = Modifier.fillMaxSize()
    )
}