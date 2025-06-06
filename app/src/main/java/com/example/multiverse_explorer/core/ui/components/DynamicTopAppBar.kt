package com.example.multiverse_explorer.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.multiverse_explorer.core.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicTopAppBar(
    navigationController: NavHostController,
    navigateBack: () -> Unit
) {

    val navBackStackEntry by navigationController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val title : String= when (currentDestination) {
        Routes.CharacterDetail.route -> "Character Detail" // TODO Change for string resource
        else -> {""}
    }

    if (currentDestination != Routes.Characters.route) {
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        )
    }
}