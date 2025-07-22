package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.database.entities.EpisodeEntity
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain


fun EpisodeEntity.toDomain(): EpisodeDomain {
    return EpisodeDomain(
        id = id,
        name = name
    )
}