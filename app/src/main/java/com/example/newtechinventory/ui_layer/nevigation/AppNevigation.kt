package com.example.newtechinventory.ui_layer.nevigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newtechinventory.ui_layer.screens.AddMemoryScreen
import com.example.newtechinventory.ui_layer.screens.GetMemoryScreenUI
import com.example.newtechinventory.viewmodel.AppViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = GetMemoryRoute) {
        composable<GetMemoryRoute> {
            GetMemoryScreenUI(
                navController = navController
            )
        }
        composable<AddMemoryRoute> {
            AddMemoryScreen(
                navController = navController
            )
        }
    }
}