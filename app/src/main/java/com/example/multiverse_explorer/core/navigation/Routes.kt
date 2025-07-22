package com.example.multiverse_explorer.core.navigation

sealed class Routes(val route: String) {
    data object Characters: Routes("home")
    data object CharacterDetail: Routes("detailCharacter/{characterId}"){
        fun createRoute(characterId: Int): String{
            return "detailCharacter/$characterId"
        }
    }
    data object Settings: Routes("settings")
}