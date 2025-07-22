package com.example.multiverse_explorer.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.multiverse_explorer.characterdetail.ui.CharacterDetailScreen
import com.example.multiverse_explorer.characters.ui.CharactersScreen
import com.example.multiverse_explorer.settings.ui.SettingsScreen


@Composable
fun NavigationWrapper(
    navigationController: NavHostController,
    modifier: Modifier
) {
    NavHost(
        navController = navigationController,
        startDestination = Routes.Characters.route
    ) {
        composable(Routes.Characters.route){
            CharactersScreen(
                navigateToCharacterDetail = { characterId: Int ->
                    navigationController.navigate(
                        Routes.CharacterDetail.createRoute(characterId)
                    )
                },
                navigateToSettings = {
                    navigationController.navigate(Routes.Settings.route)
                },
                modifier = modifier
            )
        }
        composable(
            Routes.CharacterDetail.route,
            arguments = listOf(
                navArgument("characterId") { type = NavType.IntType}
            )
        ){
            CharacterDetailScreen(
                characterId = it.arguments?.getInt("characterId") ?: 0,
                modifier = modifier
            )
        }
        composable(Routes.Settings.route){
            SettingsScreen(modifier = modifier)
        }
    }
}