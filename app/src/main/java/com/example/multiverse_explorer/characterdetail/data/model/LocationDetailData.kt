package com.example.multiverse_explorer.characterdetail.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationDetailData (
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)