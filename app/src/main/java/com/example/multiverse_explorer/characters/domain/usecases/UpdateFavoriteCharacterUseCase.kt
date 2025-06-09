package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import javax.inject.Inject

class UpdateFavoriteCharacterUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend operator fun invoke(characterId: Int, isFavorite: Boolean) =
        charactersRepository.updateFavoriteCharacter(
            characterId = characterId,
            isFavorite = isFavorite
        )
}