package com.example.multiverse_explorer.characterdetail.domain.usecases

import com.example.multiverse_explorer.characterdetail.domain.model.CharacterDetailDomain
import com.example.multiverse_explorer.characterdetail.domain.repository.CharacterDetailRepository
import com.example.multiverse_explorer.core.ResultApi
import javax.inject.Inject

class GetCharacterDetailFromNetworkUseCase @Inject constructor(private val characterDetailRepository: CharacterDetailRepository) {

    suspend operator fun invoke(characterId: Int): ResultApi<CharacterDetailDomain?> =
        characterDetailRepository.getCharacterDetailFromNetwork(characterId = characterId)

}