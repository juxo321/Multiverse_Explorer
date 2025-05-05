package com.example.multiverse_explorer.characters.data.mappers

import com.example.multiverse_explorer.characters.data.model.CharacterData
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