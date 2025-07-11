package com.example.multiverse_explorer.characters.data.network.rest.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiData (
    @Json(name = "info") val information: InformationData,
    @Json(name = "results") val characters: List<CharacterData>,
)