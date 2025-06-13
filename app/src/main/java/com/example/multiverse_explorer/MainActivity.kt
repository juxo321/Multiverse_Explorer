package com.example.multiverse_explorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.multiverse_explorer.core.navigation.NavigationWrapper
import com.example.multiverse_explorer.core.ui.components.DynamicTopAppBar
import com.example.multiverse_explorer.ui.theme.Multiverse_ExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navigationController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Multiverse_ExplorerTheme {
                navigationController = rememberNavController()
                Scaffold(
                    topBar = {
                        DynamicTopAppBar(
                            navigationController = navigationController,
                            navigateBack = { navigationController.navigateUp() }
                        )
                    },
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            NavigationWrapper(
                                navigationController = navigationController,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
