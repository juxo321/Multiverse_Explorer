package com.example.multiverse_explorer.characterdetail.domain.usecases

import javax.inject.Inject

class GetIdsFromEpisodesUseCase @Inject constructor() {

    operator fun invoke(episodes: List<String>): List<Int> {
        return episodes.map { it.split("/").last().toInt() }
    }

}