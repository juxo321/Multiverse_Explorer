package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.model.CharacterDetailData
import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain


fun CharacterDetailData.toDomain(): CharacterDetailDomain {
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