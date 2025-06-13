package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity


fun CharacterEntity.toDomain(): CharacterDetailDomain {
    return CharacterDetailDomain(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        episodes = episode
    )
}