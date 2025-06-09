package com.example.multiverse_explorer.characters.domain.usecases

import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {

    suspend operator fun invoke(selectedStatus: String): Flow<ResultApi<List<CharacterDomain>>> =
        charactersRepository.getCharactersFromDatabase(selectedStatus = selectedStatus).distinctUntilChanged()

}