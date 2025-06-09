package com.example.multiverse_explorer.characters.data.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.multiverse_explorer.characters.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.characters.data.database.entities.LocationEntity
import com.example.multiverse_explorer.characters.data.database.entities.OriginEntity

data class CharacterWithRelations (
    @Embedded val character: CharacterEntity,

    @Relation(
        parentColumn = "originId",
        entityColumn = "id"
    )
    val origin: OriginEntity,


    @Relation(
        parentColumn = "locationId",
        entityColumn = "id"
    )
    val location: LocationEntity
)