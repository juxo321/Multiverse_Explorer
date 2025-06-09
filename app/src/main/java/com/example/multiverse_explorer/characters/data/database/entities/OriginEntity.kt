package com.example.multiverse_explorer.characters.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.multiverse_explorer.core.Constants.Database.ORIGIN_TABLE


@Entity( tableName = ORIGIN_TABLE)
data class OriginEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "url") val url: String
)