package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.database.entities.EpisodeEntity
import com.example.multiverse_explorer.characterdetail.data.network.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain


fun EpisodeData.toDomain(): EpisodeDomain {
    return EpisodeDomain(
        id = id,
        name = name
    )
}


fun EpisodeData.toEntity(): EpisodeEntity {
    return EpisodeEntity(
        id = id,
        name = name,
        airDate = airDate,
        episode = episode,
        characters = characters,
        url = url,
        created = created
    )
}