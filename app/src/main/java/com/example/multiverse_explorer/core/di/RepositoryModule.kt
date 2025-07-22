package com.example.multiverse_explorer.core.di

import com.example.multiverse_explorer.characterdetail.data.CharacterDetailRepositoryImp
import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.network.CharacterDetailService
import com.example.multiverse_explorer.characterdetail.data.network.EpisodeService
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.characters.data.CharactersRepositoryImp
import com.example.multiverse_explorer.characters.data.database.dao.CharacterDao
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesCharactersRepository(
        charactersService: CharactersService,
        characterDao: CharacterDao
    ): CharactersRepository =
        CharactersRepositoryImp(charactersService, characterDao)

    @Singleton
    @Provides
    fun providesCharacterDetailRepository(
        characterDetailService: CharacterDetailService,
        characterDetailDao: CharacterDetailDao,
        episodeService: EpisodeService
    ): CharacterDetailRepository =
        CharacterDetailRepositoryImp(characterDetailService, characterDetailDao,episodeService)

}