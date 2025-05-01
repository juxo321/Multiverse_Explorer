package com.example.multiverse_explorer.core.di

import com.example.multiverse_explorer.characters.data.CharactersRepositoryImp
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
    fun providesCharactersRepository(charactersService: CharactersService): CharactersRepository =
        CharactersRepositoryImp(charactersService)

}