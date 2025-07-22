package com.example.multiverse_explorer.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.database.entities.EpisodeEntity
import com.example.multiverse_explorer.characters.data.database.dao.CharacterDao
import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.characters.data.database.entities.LocationEntity
import com.example.multiverse_explorer.characters.data.database.entities.OriginEntity

@Database(
    entities = [CharacterEntity::class, OriginEntity::class, LocationEntity::class ,EpisodeEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class CharacterDatabase: RoomDatabase() {

    abstract fun getCharacterDao(): CharacterDao
    abstract fun getCharacterDetailDao(): CharacterDetailDao
}