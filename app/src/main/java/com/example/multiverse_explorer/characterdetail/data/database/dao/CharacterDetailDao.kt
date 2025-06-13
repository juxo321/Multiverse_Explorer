package com.example.multiverse_explorer.characterdetail.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.multiverse_explorer.characterdetail.data.database.entities.EpisodeEntity
import com.example.multiverse_explorer.core.data.database.entities.CharacterEntity

@Dao
interface CharacterDetailDao {

    @Query("SELECT * FROM character_table WHERE id = :characterId")
    fun getCharacterDetail(characterId: Int): CharacterEntity?

    @Query("SELECT * FROM episode_table WHERE id IN (:episodeIds)")
    fun getEpisodes(episodeIds: List<Int>): List<EpisodeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(episodes: List<EpisodeEntity>)

}