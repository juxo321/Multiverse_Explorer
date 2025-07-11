package com.example.multiverse_explorer.characters.data.mappers

import com.example.multiverse_explorer.characters.data.network.rest.model.CharacterData
import com.example.multiverse_explorer.characters.data.network.rest.model.LocationData
import com.example.multiverse_explorer.characters.data.network.rest.model.OriginData
import com.multiverse_explorer.GetCharactersQuery


fun GetCharactersQuery.Result.toCharacterData(): CharacterData {
    return CharacterData(
        id = id?.toInt() ?: 0,
        name = name ?: "",
        status = status ?: "",
        species = species ?: "",
        type = type ?: "",
        gender = gender ?: "",
        origin = OriginData(
            name = origin?.name ?: "",
            url = "\"https://rickandmortyapi.com/api/location/${id}"
        ),
        location = LocationData(
            name = location?.name ?: "",
            url = "https://rickandmortyapi.com/api/location/${id}"
        ),
        image = image ?: "",
        episode = episode.map { "https://rickandmortyapi.com/api/episode/${it?.id}" },
        url = "https://rickandmortyapi.com/api/character/${id}",
        created = created ?: ""
    )
}