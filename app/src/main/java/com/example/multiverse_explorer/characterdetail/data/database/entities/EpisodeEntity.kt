package com.example.multiverse_explorer.characterdetail.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.multiverse_explorer.core.Constants.Database.EPISODE_TABLE

@Entity(tableName = EPISODE_TABLE)
data class EpisodeEntity (
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "air_date") val airDate: String,
    @ColumnInfo(name = "episode") val episode: String,
    @ColumnInfo(name = "characters") val characters: List<String>,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "created") val created: String,
)