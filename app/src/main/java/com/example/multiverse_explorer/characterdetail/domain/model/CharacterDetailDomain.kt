package com.example.multiverse_explorer.characterdetail.domain.model

data class CharacterDetailDomain (
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val episodes: List<String>
)