package com.example.newtechinventory

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.newtechinventory.ui.theme.NewtechInventoryTheme
import com.example.newtechinventory.ui_layer.nevigation.AppNavigation
import com.example.newtechinventory.ui_layer.screens.AddMemoryScreen
import com.example.newtechinventory.ui_layer.screens.AddProductScreen
import com.example.newtechinventory.ui_layer.screens.GetMemoryScreenUI
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewtechInventoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
//                        GetProductsScreen()
                        AppNavigation()
//                        GetMemoryScreenUI()
//                        AddMemoryScreen()
                    }
                }
            }
        }
    }
}
