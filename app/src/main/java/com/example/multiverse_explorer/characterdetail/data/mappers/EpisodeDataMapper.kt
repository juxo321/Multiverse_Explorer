package com.example.multiverse_explorer.characterdetail.data.mappers

import com.example.multiverse_explorer.characterdetail.data.model.EpisodeData
import com.example.multiverse_explorer.characterdetail.domain.model.EpisodeDomain


fun EpisodeData.toDomain(): EpisodeDomain {
    return EpisodeDomain(
        id = id,
        name = name
    )
}