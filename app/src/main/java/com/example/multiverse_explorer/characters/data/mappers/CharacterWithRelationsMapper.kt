package com.example.multiverse_explorer.characters.data.mappers

import com.example.multiverse_explorer.characters.data.database.CharacterWithRelations
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain

fun CharacterWithRelations.toDomain(): CharacterDomain {
    return CharacterDomain(
        id = character.originId,
        name = character.name,
        status = character.status,
        species = character.species,
        image = character.image,
        favorite = character.isFavorite
    )
}