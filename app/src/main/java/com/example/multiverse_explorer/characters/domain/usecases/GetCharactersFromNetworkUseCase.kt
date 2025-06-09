package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import javax.inject.Inject

class GetCharactersFromNetworkUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend operator fun invoke(selectedStatus: String) =
        charactersRepository.getCharactersFromNetwork(selectedStatus = selectedStatus)

}