package com.example.multiverse_explorer.core.navigation

import okhttp3.Route

sealed class Routes(val route: String) {
    object Characters: Routes("home")
    object CharacterDetail: Routes("detailCharacter/{characterId}"){
        fun createRoute(characterId: Int): String{
            return "detailCharacter/$characterId"
        }
    }
}