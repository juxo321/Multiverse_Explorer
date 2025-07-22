package com.example.multiverse_explorer.characters.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.multiverse_explorer.characters.data.database.CharacterWithRelations
import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity
import com.example.multiverse_explorer.characters.data.database.entities.LocationEntity
import com.example.multiverse_explorer.characters.data.database.entities.OriginEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Transaction
    @Query("SELECT * FROM character_table WHERE status = :status OR :status = ''")
    fun getCharactersByStatus(status: String): Flow<List<CharacterWithRelations>>

    @Query("SELECT * FROM character_table WHERE isFavorite = 1")
    fun getFavoriteCharacters(): List<CharacterWithRelations>

    @Query("SELECT * FROM character_table")
    fun getAllCharacters(): Flow<List<CharacterWithRelations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrigins(origins: List<OriginEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocations(locations: List<LocationEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharacterEntity>): List<Long>

    @Query("UPDATE character_table SET isFavorite = :isFavorite WHERE id = :characterId")
    fun updateFavoriteCharacter(characterId: Int, isFavorite:Boolean): Int

    @Query("DELETE FROM character_table")
    suspend fun deleteAllCharacters()

    @Query("DELETE FROM location_table")
    suspend fun deleteAllLocations()

    @Query("DELETE FROM origin_table")
    suspend fun deleteAllOrigins()

    @Query("DELETE FROM episode_table")
    suspend fun deleteAllEpisodes()

    @Transaction
    suspend fun clearAllData() {
        deleteAllLocations()
        deleteAllOrigins()
        deleteAllEpisodes()
        deleteAllCharacters()
    }

}