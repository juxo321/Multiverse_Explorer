package com.example.multiverse_explorer.characters.data

import com.example.multiverse_explorer.characters.data.database.CharacterWithRelations
import com.example.multiverse_explorer.characters.data.database.dao.CharacterDao
import com.example.multiverse_explorer.characters.data.database.entities.LocationEntity
import com.example.multiverse_explorer.characters.data.database.entities.OriginEntity
import com.example.multiverse_explorer.characters.data.mappers.toDomain
import com.example.multiverse_explorer.characters.data.mappers.toEntity
import com.example.multiverse_explorer.characters.data.network.CharactersService
import com.example.multiverse_explorer.characters.data.network.model.CharacterData
import com.example.multiverse_explorer.characters.domain.model.CharacterDomain
import com.example.multiverse_explorer.characters.domain.repository.CharactersRepository
import com.example.multiverse_explorer.core.ResultApi
import com.example.multiverse_explorer.core.utils.NetworkFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharactersRepositoryImp @Inject constructor(
    private val charactersService: CharactersService,
    private val characterDao: CharacterDao
) : CharactersRepository {

    override suspend fun getCharactersFromDatabase(selectedStatus: String): Flow<ResultApi<List<CharacterDomain>>> {
        return characterDao.getCharactersByStatus(status = selectedStatus)
            .map<List<CharacterWithRelations>, ResultApi<List<CharacterDomain>>> { characters ->
                ResultApi.Success(characters.map { it.toDomain() })
            }
            .catch { e ->
                emit(ResultApi.Error(e.message ?: "Something went wrong, try again!"))
            }
    }

    override suspend fun getCharactersFromNetwork(selectedStatus: String) {
        val result = NetworkFunctions.safeServiceCallCharacters(
            serviceCall = { charactersService.getCharacters(selectedStatus = selectedStatus) },
        )

        if (result is ResultApi.Success) {
            saveCharactersToDatabase(result.data)
        }

    }

    override suspend fun saveCharactersToDatabase(charactersData: List<CharacterData>) {

        val favoriteCharacters = characterDao.getFavoriteCharacters()

        val origins = charactersData.map { characterData ->
            OriginEntity(
                name = characterData.origin.name,
                url = characterData.origin.url
            )
        }

        val locations = charactersData.map { characterData ->
            LocationEntity(
                name = characterData.location.name,
                url = characterData.location.url
            )
        }

        val originIds = characterDao.insertOrigins(origins)
        val locationIds = characterDao.insertLocations(locations)

        val characters = charactersData.mapIndexed { index, characterData ->
            characterData.toEntity(
                originId = originIds[index].toInt(),
                locationId = locationIds[index].toInt()
            ).copy(isFavorite = favoriteCharacters.any { it.character.id == characterData.id })
        }

        characterDao.insertCharacters(characters)
    }


    override suspend fun updateFavoriteCharacter(characterId: Int, isFavorite: Boolean) =
        withContext(Dispatchers.IO) {
            characterDao.updateFavoriteCharacter(characterId = characterId, isFavorite = isFavorite)
        }

    override suspend fun clearAllData() {
        withContext(Dispatchers.IO){
            characterDao.clearAllData()
        }
    }


}