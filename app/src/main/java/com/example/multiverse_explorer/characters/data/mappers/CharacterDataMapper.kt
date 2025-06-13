package com.example.multiverse_explorer.characters.data.mappers

import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.characters.data.network.model.CharacterData
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain


fun CharacterData.toDomain(): CharacterDomain {
    return CharacterDomain(
        id = id,
        name = name,
        status = status,
        species = species,
        image = image
    )
}


fun CharacterData.toEntity(originId: Int, locationId: Int): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originId = originId,
        locationId = locationId,
        image = image,
        episode = episode,
        url = url,
        created = created,
        isFavorite = false
    )
}