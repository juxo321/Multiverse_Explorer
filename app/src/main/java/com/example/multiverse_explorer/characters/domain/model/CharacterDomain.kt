package com.example.multiverse_explorer.characters.domain.model

data class CharacterDomain (
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String,
    val favorite: Boolean = false
)