package com.example.multiverse_explorer.core.data.database

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromEpisodeList(episodes: List<String>): String = episodes.joinToString(",")

    @TypeConverter
    fun toEpisodeList(episodesString: String): List<String> =
        if (episodesString.isEmpty()) emptyList() else episodesString.split(",")
}