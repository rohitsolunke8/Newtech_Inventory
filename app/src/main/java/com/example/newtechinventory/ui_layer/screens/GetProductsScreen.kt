package com.example.newtechinventory.ui_layer.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newtechinventory.data_layer.network.responce.Data
import com.example.newtechinventory.data_layer.network.responce.DataX
import com.example.newtechinventory.data_layer.network.responce.DataXXX
import com.example.newtechinventory.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun GetProductsScreen(modifier: Modifier = Modifier, viewModel: AppViewModel = hiltViewModel()) {

    val state = viewModel.getProductsState.collectAsState()
    val products = state.value.data?.body()?.data ?: emptyList()

    val categoryState = viewModel.getCategoryState.collectAsState()
    val categories = categoryState.value.data?.body()?.data ?: emptyList()

    val locationState = viewModel.getLocationState.collectAsState()
    val locations = locationState.value.data?.body()?.data ?: emptyList()

    // State for search query
    val modelNumber = remember { mutableStateOf("") }

    // Filter products based on the search query
    val filteredProducts = products.filter {
        it.model_number.contains(modelNumber.value, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Search Bar
        OutlinedTextField(
            value = modelNumber.value,
            onValueChange = { modelNumber.value = it },
            label = { Text("Search by Model Number") },
            modifier = Modifier.fillMaxWidth()
        )

        when {
            state.value.loading -> Text("Loading...")
            state.value.error != null -> Text("Error: ${state.value.error}")
            filteredProducts.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(filteredProducts.size) { index ->
                        val product = filteredProducts[index]
                        OutlinedCardI(product, categories, locations)
                    }
                }
            }
            else -> Text("No products found.")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedCardI(product: Data, categories: List<DataX>, locations: List<DataXXX>) {

    val result = if (product.quantity_in_stock > 1) {
        Color(0xFF87FF00)
    } else {
        Color(0xFFFF2020)
    }

    OutlinedCard(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = product.product_name, color = Color.Black)

                Text(
                    text = product.quantity_in_stock.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = result,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                )
            }
            Text(text = product.model_number, style = MaterialTheme.typography.headlineMedium)
            Text(product.serial_number)
//            Text(locations.elementAt(product.product_id).shelf)
            Text(product.updated_at)
            Text(product.description)
        }
    }
}


