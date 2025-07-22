package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.network.rest.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.LocationDetailData
import com.example.multiverse_explorer.characterdetail.data.network.rest.model.OriginDetailData
import com.multiverse_explorer.GetCharacterDetailQuery

fun GetCharacterDetailQuery.Character.toData(): CharacterDetailData{
    return CharacterDetailData(
        id = id?.toInt() ?: 0,
        name = name ?: "",
        status = status ?: "",
        species = species ?: "",
        type = type ?: "",
        gender = gender ?: "",
        origin = OriginDetailData(
            name = origin?.name ?: "",
            url = "\"https://rickandmortyapi.com/api/location/${id}"
        ),
        location = LocationDetailData(
            name = location?.name ?: "",
            url = "https://rickandmortyapi.com/api/location/${id}"
        ),
        image = image ?: "",
        episode = episode.map { "https://rickandmortyapi.com/api/episode/${it?.id}" },
        url = "https://rickandmortyapi.com/api/character/${id}",
        created = created ?: ""
    )
}