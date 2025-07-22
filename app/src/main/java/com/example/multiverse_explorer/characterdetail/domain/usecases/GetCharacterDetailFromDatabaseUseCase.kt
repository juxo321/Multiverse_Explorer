package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import javax.inject.Inject

class GetCharacterDetailFromDatabaseUseCase @Inject constructor(private val characterDetailRepository: CharacterDetailRepository) {

    suspend operator fun invoke(characterId: Int) =
        characterDetailRepository.getCharacterDetailFromDatabase(characterId = characterId)
}