package com.example.multiverse_explorer.core.di

import com.example.multiverse_explorer.characterdetail.data.CharacterDetailRepositoryImp
import com.example.multiverse_explorer.characterdetail.data.database.dao.CharacterDetailDao
import com.example.multiverse_explorer.characterdetail.data.network.graphql.CharacterDetailGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.graphql.EpisodeGraphQLDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.CharacterDetailRestDataSource
import com.example.multiverse_explorer.characterdetail.data.network.rest.EpisodeRestDataSource
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.characters.data.CharactersRepositoryImp
import com.example.multiverse_explorer.characters.data.database.dao.CharacterDao
import com.example.multiverse_explorer.characters.data.network.grqphql.CharactersGraphQLDataSource
import com.example.multiverse_explorer.characters.data.network.rest.CharactersRestDataSource
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.data.datastore.SettingsDataStore
import com.example.multiverse_explorer.settings.data.SettingsRepositoryImp
import com.example.multiverse_explorer.settings.domain.repository.SettingsRepository
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
        settingsDataStore: SettingsDataStore,
        charactersRestDataSource: CharactersRestDataSource,
        charactersGraphQLDataSource: CharactersGraphQLDataSource,
        characterDao: CharacterDao
    ): CharactersRepository =
        CharactersRepositoryImp(
            settingsDataStore = settingsDataStore,
            charactersRestDataSource = charactersRestDataSource,
            charactersGraphQLDataSource = charactersGraphQLDataSource,
            characterDao = characterDao
        )

    @Singleton
    @Provides
    fun providesCharacterDetailRepository(
        settingsDataStore: SettingsDataStore,
        characterDetailRestDataSource: CharacterDetailRestDataSource,
        characterDetailGraphQLDataSource: CharacterDetailGraphQLDataSource,
        episodeRestDataSource: EpisodeRestDataSource,
        episodeGraphQLDataSource: EpisodeGraphQLDataSource,
        characterDetailDao: CharacterDetailDao,
    ): CharacterDetailRepository =
        CharacterDetailRepositoryImp(
            settingsDataStore = settingsDataStore,
            characterDetailRestDataSource = characterDetailRestDataSource,
            characterDetailGraphQLDataSource = characterDetailGraphQLDataSource,
            episodeRestDataSource = episodeRestDataSource,
            episodeGraphQLDataSource = episodeGraphQLDataSource,
            characterDetailDao = characterDetailDao
        )


    @Singleton
    @Provides
    fun providesSettingsRepository(settingsDataStore: SettingsDataStore): SettingsRepository =
        SettingsRepositoryImp(settingsDataStore)

}