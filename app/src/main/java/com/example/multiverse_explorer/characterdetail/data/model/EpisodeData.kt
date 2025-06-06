package com.example.multiverse_explorer.characterdetail.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class EpisodeData (
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "air_date") val airDate: String,
    @Json(name = "episode") val episode: String,
    @Json(name = "characters") val characters: List<String>,
    @Json(name = "url") val url: String,
    @Json(name = "created") val created: String,
)