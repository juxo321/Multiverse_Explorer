package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.network.rest.model.EpisodeData
import com.multiverse_explorer.GetEpisodesQuery

fun GetEpisodesQuery.EpisodesById.toEpisodeData(): EpisodeData {
    return EpisodeData(
        id = id?.toInt() ?: 0,
        name = name ?: "",
        airDate = air_date ?: "",
        episode = episode ?: "",
        characters = characters.map { "https://rickandmortyapi.com/api/character/${it}" },
        url = "https://rickandmortyapi.com/api/episode/${id}",
        created = created ?: ""
    )
}