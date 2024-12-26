package com.example.newtechinventory.ui_layer.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newtechinventory.ui_layer.nevigation.GetProductRoute
import com.example.newtechinventory.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun AddProductScreen(
    navController: NavHostController,
    viewModel: AppViewModel = hiltViewModel()
) {

    val state = viewModel.addProductState.collectAsState()
    val categoryState = viewModel.getCategoryState.collectAsState()
    val context = LocalContext.current

    val locationState = viewModel.getLocationState.collectAsState()

    var productName by remember { mutableStateOf("") }
    var modelNumber by remember { mutableStateOf("") }
    var serialNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf("") }

    var isExpanded by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf("") }

    var locationId by remember { mutableStateOf("") }

    var selectedWarehouseName by remember { mutableStateOf("") }
    var isExpandedForWarehouse by remember { mutableStateOf(false) }

    var selectedShelfName by remember { mutableStateOf("") }
    var isExpandedForShelf by remember { mutableStateOf(false) }

    var selectedAisle by remember { mutableStateOf("") }
    var isExpandedForAisle by remember { mutableStateOf(false) }



    val scrollState = rememberScrollState()

    LaunchedEffect(state.value, Unit) {
        when {
            state.value.error != null -> {
                Toast.makeText(context, state.value.error, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "$state.value.error")
                viewModel.resetState()
            }

            state.value.data != null -> {
                Toast.makeText(context, state.value.data!!.body()?.message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.resetState()
//                viewModel.addProduct(
//                    productName = "",
//                    categoryId = "",
//                    modelNumber = "",
//                    serialNumber = "",
//                    location = "",
//                    quantity = "",
//                    description = "",
//                    status = ""
//                )
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            singleLine = true,
            label = { Text(text = "Enter product name") }
        )

        ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
            OutlinedTextField(
                value = selectedCategoryName,
                singleLine = true,
                label = { Text("Select category") },
                onValueChange = { categoryId = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                categoryState.value.data?.body()?.data?.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.category_name) },
                        onClick = {
                            categoryId = category.category_id.toString()
                            selectedCategoryName = category.category_name
                            isExpanded = false
                        }
                    )
                }
            }
        }


        OutlinedTextField(
            value = modelNumber,
            onValueChange = { modelNumber = it },
            singleLine = true,
            label = { Text(text = "Enter model number") }
        )
        OutlinedTextField(
            value = serialNumber,
            onValueChange = { serialNumber = it },
            singleLine = true,
            label = { Text(text = "Enter serial number") }
        )
//        OutlinedTextField(
//            value = location,
//            onValueChange = { location = it },
//            singleLine = true,
//            label = { Text(text = "Enter location") }
//        )

        ExposedDropdownMenuBox(expanded = isExpandedForWarehouse, onExpandedChange = { isExpandedForWarehouse = it }) {
            OutlinedTextField(
                value = selectedWarehouseName,
                singleLine = true,
                label = { Text("Select Warehouse Name") },
                onValueChange = { locationId = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForWarehouse)
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
//                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = isExpandedForWarehouse, onDismissRequest = { isExpandedForWarehouse = false }) {
                locationState.value.data?.body()?.data?.forEach { location ->
                    DropdownMenuItem(
                        text = { Text(location.warehouse_name) },
                        onClick = {
                            locationId = location.location_id.toString()
                            selectedWarehouseName = location.warehouse_name
                            isExpandedForWarehouse = false
                            navController.navigate(GetProductRoute)
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(expanded = isExpandedForShelf, onExpandedChange = { isExpandedForShelf = it }) {
            OutlinedTextField(
                value = selectedShelfName,
                singleLine = true,
                label = { Text("Select Shelf") },
                onValueChange = { locationId = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForShelf)
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = isExpandedForShelf, onDismissRequest = { isExpandedForShelf = false }) {
                locationState.value.data?.body()?.data?.forEach { location ->
                    DropdownMenuItem(
                        text = { Text(location.shelf) },
                        onClick = {
                            locationId = location.location_id.toString()
                            selectedShelfName = location.shelf
                            isExpandedForShelf = false
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(expanded = isExpandedForAisle, onExpandedChange = { isExpandedForAisle = it }) {
            OutlinedTextField(
                value = selectedAisle,
                singleLine = true,
                label = { Text("Select Aisle") },
                onValueChange = { locationId = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpandedForAisle)
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = isExpandedForAisle, onDismissRequest = { isExpandedForAisle = false }) {
                locationState.value.data?.body()?.data?.forEach { location ->
                    DropdownMenuItem(
                        text = { Text(location.aisle) },
                        onClick = {
                            locationId = location.location_id.toString()
                            selectedAisle = location.aisle
                            isExpandedForAisle = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            singleLine = true,
            label = { Text(text = "Enter quantity") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            singleLine = true,
            label = { Text(text = "Enter description") }
        )
//        OutlinedTextField(
//            value = status,
//            onValueChange = { status = it },
//            singleLine = true,
//            label = { Text(text = "Enter status") }
//        )
        Button(
            modifier = Modifier.width(100.dp),
            onClick = {
                if (productName.isNotBlank() && categoryId.isNotBlank() && modelNumber.isNotBlank() && serialNumber.isNotBlank() /* && quantity.isNotEmpty()*/ && description.isNotEmpty() /*&& status.isNotEmpty()*/) {
                    viewModel.addProduct(
                        productName = productName,
                        categoryId = categoryId,
                        modelNumber = modelNumber,
                        serialNumber = serialNumber,
//                        location = location,
                        quantity = quantity,
                        description = description,
                        warehouseName = selectedWarehouseName,
                        aisle = selectedShelfName,
                        shelf = selectedAisle
                    )
//                    navController.navigate(GetProductsScreen())
                } else {
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            if (state.value.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                viewModel.resetState()
            } else {
                Text(
                    text = "Submit"
                )
            }
        }
    }
}
