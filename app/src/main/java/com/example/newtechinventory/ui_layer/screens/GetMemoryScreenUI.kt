package com.example.newtechinventory.ui_layer.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.twotone.FilterAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newtechinventory.data_layer.network.responce.DataXXXX
import com.example.newtechinventory.ui_layer.nevigation.AddMemoryRoute
import com.example.newtechinventory.viewmodel.AppViewModel

@Composable
fun GetMemoryScreenUI(viewModel: AppViewModel = hiltViewModel(), navController: NavHostController) {

    val context = LocalContext.current

    val getMemoryState = viewModel.getMemoryState.collectAsState()
    val searchedMemoryState = viewModel.getSearchedMemoryState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    val filters = remember { mutableStateOf(DataXXXX) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(AddMemoryRoute)
                }
            ) {
                Icon(Icons.Outlined.Add, null)
            }
        }
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    singleLine = true,
                    label = { Text("Memory part number") },
                    onValueChange = { searchQuery = it },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            isSearching = searchQuery.isNotBlank()
                            Log.d("printFilter", searchQuery)
                            if (isSearching) {
                                val filter = mapOf(
                                    "memory_part_number" to searchQuery
                                )
                                viewModel.searchedMemoryRequest(filters = filter)
                                Log.d("printFilter", "$filter")
                            }
                        }
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isSearching = searchQuery.isNotBlank()
                                Log.d("printFilter", searchQuery)
                                if (isSearching) {
                                    val filter = mapOf(
                                        "memory_part_number" to searchQuery
                                    )
                                    viewModel.searchedMemoryRequest(filters = filter)
                                    Log.d("printFilter", "$filter")
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null
                            )
                        }
                    },
                )
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = null,
                        Modifier.size(48.dp)
                    )
                }
            }
            when {
                getMemoryState.value.loading || searchedMemoryState.value.loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                getMemoryState.value.error != null || searchedMemoryState.value.error != null -> {
                    Toast.makeText(context, "${getMemoryState.value.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                isSearching && searchedMemoryState.value.data?.body().isNullOrEmpty() -> {
                    Text(
                        text = "No search results found.",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                !isSearching && getMemoryState.value.data?.body()?.data.isNullOrEmpty() -> {
                    Text(
                        text = "No memory data available",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                else -> {
                    val allMemories = if (isSearching) {
                        searchedMemoryState.value.data?.body() ?: emptyList()
                    } else {
                        getMemoryState.value.data?.body()?.data ?: emptyList()
                    }

                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(allMemories) { memory ->
                            OutlinedCard(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth(),
                                border = BorderStroke(1.dp, Color.Black)
                            ) {

                                Column(
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text(
                                        text = "Brand: ${memory.memory_brand}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = "Capacity: ${memory.memory_capacity}")
                                    Text(text = "Speed: ${memory.memory_speed}")
                                    Text(text = "Type: ${memory.memory_type}")
                                    Text(text = "Part Number: ${memory.memory_part_number}")
                                    Text(text = "Serial Number: ${memory.memory_serial_number}")
                                    Text(
                                        text = "Status: ${memory.status}",
//                                      color = Color.Green
                                    )
                                    Text(text = "Description: ${memory.description}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
