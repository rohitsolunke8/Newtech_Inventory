package com.example.newtechinventory.ui_layer.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newtechinventory.viewmodel.AppViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.scopes.ViewModelScoped

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryScreen(viewModel: AppViewModel = hiltViewModel(), navController: NavHostController) {

    val dropdownState = viewModel.memoryDropdownState.collectAsState()
    val filteredSuggestions = viewModel.filteredSuggestions.collectAsState()

    val addMemoryState = viewModel.addMemorySate.collectAsState()

    var memoryBrands by remember { mutableStateOf("") }
    var brandExpanded by remember { mutableStateOf(false) }

    var memoryType by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }

    var memoryCapacity by remember { mutableStateOf("") }
    var capacityExpanded by remember { mutableStateOf(false) }

    var memoryPartNumber by remember { mutableStateOf(mutableListOf("")) }
    var partNumberExpanded by remember { mutableStateOf(false) }

    var memoryVoltage by remember { mutableStateOf("") }
    var voltageExpanded by remember { mutableStateOf(false) }

    var numberOfModules by remember { mutableStateOf("") }
    var modulesExpanded by remember { mutableStateOf(false) }

    var memorySpeed by remember { mutableStateOf("") }
    var speedExpanded by remember { mutableStateOf(false) }

    var rawValue by remember { mutableStateOf("") }

    var memoryDescription by remember { mutableStateOf("") }

    var memorySerialNumber by remember { mutableStateOf(mutableListOf("")) }

    val interactionSource = remember { MutableInteractionSource() }


    val context = LocalContext.current

    var scanner = viewModel.barcodeScanner(context = context).startScan()

//    var scanner = GmsBarcodeScanning.getClient(context)

//    val heightTextFields by remember { mutableStateOf(55.dp) }
//    var textFieldSize by remember { mutableStateOf(Size.Zero) }


    LaunchedEffect(Unit) {
        viewModel.memoryDropdown()
    }

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
//        .clickable(
//            interactionSource = interactionSource,
//            indication = null,
//            onClick = { brandExpanded = false } // Dismiss dropdown when clicking outside
//        )
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = "Add Memory"
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Brand") },
                    value = memoryBrands,
                    onValueChange = {
                        memoryBrands = it
                        brandExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "memory_brand", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { brandExpanded = !brandExpanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
//                                tint = Color.Black
                            )
                        }
                    })
            }
            AnimatedVisibility(visible = brandExpanded) {
                Card(
                    Modifier.fillMaxWidth()
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp)

                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    memoryBrands = selected // Set the selected suggestion
                                    brandExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_brands ?: emptyList()
                            ) { brand ->
                                CategoryItems(title = brand) { selected ->
                                    memoryBrands = selected
                                    brandExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
//                        textFieldSize = coordinates.size.toSize()
                    }, label = { Text("Memory Type") }, value = memoryType, onValueChange = {
                    memoryType = it
                    typeExpanded = it.isNotEmpty()
                    viewModel.filterSuggestions(
                        "memoryType", it
                    ) // Dynamically filter suggestions
                }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ), singleLine = true, trailingIcon = {
                    IconButton(onClick = { typeExpanded = !typeExpanded }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "arrow",
//                                tint = Color.Black
                        )
                    }
                })
            }
            AnimatedVisibility(visible = typeExpanded) {
                Card {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp)
                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    memoryType = selected // Set the selected suggestion
                                    typeExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_types ?: emptyList()
                            ) { type ->
                                CategoryItems(title = type) { selectedType ->
                                    memoryType = selectedType
                                    typeExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Capacity") },
                    value = memoryCapacity,
                    onValueChange = {
                        memoryCapacity = it
                        capacityExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "memory_capacity", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { capacityExpanded = !capacityExpanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
//                                tint = Color.Black
                            )
                        }
                    })
            }
            AnimatedVisibility(visible = capacityExpanded) {
                Card {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    memoryCapacity = selected // Set the selected suggestion
                                    capacityExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_capacities ?: emptyList()
                            ) { type ->
                                CategoryItems(title = type) { selectedType ->
                                    memoryCapacity = selectedType
                                    capacityExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Voltage") },
                    value = memoryVoltage,
                    onValueChange = {
                        memoryVoltage = it
                        voltageExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "memory_voltage", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { voltageExpanded = !voltageExpanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
//                                tint = Color.Black
                            )
                        }
                    })
            }
            AnimatedVisibility(visible = voltageExpanded) {
                Card {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    memoryVoltage = selected // Set the selected suggestion
                                    voltageExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_voltage ?: emptyList()
                            ) { type ->
                                CategoryItems(title = type) { selectedVoltage ->
                                    memoryVoltage = selectedVoltage
                                    voltageExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Modules") },
                    value = numberOfModules,
                    onValueChange = {
                        numberOfModules = it
                        modulesExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "number_of_modules", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { modulesExpanded = !modulesExpanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
//                                tint = Color.Black
                            )
                        }
                    })
            }
            AnimatedVisibility(visible = modulesExpanded) {
                Card {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    numberOfModules = selected // Set the selected suggestion
                                    modulesExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_modules ?: emptyList()
                            ) { type ->
                                CategoryItems(title = type.toString()) { selectedModules ->
                                    numberOfModules = selectedModules
                                    modulesExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Speed") },
                    value = memorySpeed,
                    onValueChange = {
                        memorySpeed = it
                        speedExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "memory_speed", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { speedExpanded = !speedExpanded }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
//                                tint = Color.Black
                            )
                        }
                    })
            }
            AnimatedVisibility(visible = speedExpanded) {
                Card {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {
                        if (filteredSuggestions.value.isNotEmpty()) {
                            items(filteredSuggestions.value.sorted()) { suggestion ->
                                CategoryItems(title = suggestion) { selected ->
                                    memorySpeed = selected // Set the selected suggestion
                                    speedExpanded = false       // Close the dropdown
                                }
                            }
                        } else {
                            items(
                                dropdownState.value.data?.body()?.memory_speed ?: emptyList()
                            ) { type ->
                                CategoryItems(title = type) { selectedSpeed ->
                                    memorySpeed = selectedSpeed
                                    speedExpanded = false
                                }
                            }
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Memory Description") },
                    value = memoryDescription,
                    onValueChange = {
                        memoryDescription = it
//                        serialNumberExpanded = it.isNotEmpty()
                        viewModel.filterSuggestions(
                            "description", it
                        ) // Dynamically filter suggestions
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                )
            }

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                items(memoryPartNumber.size) { index ->
                    Column(

                    ) {

                        OutlinedTextField(
                            value = rawValue,
                            onValueChange = { newPart ->
                                memoryPartNumber = memoryPartNumber.toMutableList().apply {
                                    this[index] = newPart
                                }
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    scanner.addOnSuccessListener { barcode ->
                                            rawValue = barcode.rawValue.toString()
                                            Toast.makeText(
                                                context, rawValue, Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }) {
                                    Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                                }
                            },

                            label = { Text("Memory Part Number") },
                            modifier = Modifier.padding(end = 8.dp)

                        )

                        // Memory Serial Number Field
                        OutlinedTextField(
                            value = memorySerialNumber.getOrElse(index) { "" },
                            onValueChange = { newSerial ->
                                memorySerialNumber = memorySerialNumber.toMutableList().apply {
                                    if (index < size) this[index] = newSerial else add(newSerial)
                                }
                            },
                            label = { Text("Memory Serial Number") },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    // Remove Button
                    IconButton(onClick = {
                        memoryPartNumber =
                            memoryPartNumber.toMutableList().apply { removeAt(index) }
                        memorySerialNumber =
                            memorySerialNumber.toMutableList().apply { removeAt(index) }
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Remove Entry")
                    }
                }

                // Add Button at the End
                item {
                    IconButton(onClick = {
                        memoryPartNumber = memoryPartNumber.toMutableList().apply { add("") }
                        memorySerialNumber = memorySerialNumber.toMutableList().apply { add("") }

                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Entry")
                    }
                }
            }
        }

        Button(onClick = {
            if (memoryBrands.isNotBlank() && memoryType.isNotBlank() && memoryCapacity.isNotBlank() && memoryPartNumber.all { it.isNotBlank() } && memoryVoltage.isNotBlank() && numberOfModules.isNotBlank() && memorySpeed.isNotBlank() && memoryDescription.isNotBlank() && memorySerialNumber.all { it.isNotBlank() }) {
                viewModel.addMemory(
                    memoryBrands,
                    memoryType,
                    memoryCapacity,
                    memoryPartNumber,
                    memoryVoltage,
                    numberOfModules,
                    memorySpeed,
                    memoryDescription,
                    memorySerialNumber
                )
            } else {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            }
//            navController.navigate(GetMemoryRoute)
        }) {
            Text(text = "Add Memory")
        }
        when {
            addMemoryState.value.error != null -> {
                Toast.makeText(context, addMemoryState.value.error, Toast.LENGTH_SHORT).show()
            }

            addMemoryState.value.data != null -> {
                Toast.makeText(
                    context,
                    addMemoryState.value.data!!.body()?.memory_serial_number.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun CategoryItems(
    title: String, onSelect: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelect(title) }
        .padding(10.dp)) {
        Text(text = title, fontSize = 16.sp)
    }
}



