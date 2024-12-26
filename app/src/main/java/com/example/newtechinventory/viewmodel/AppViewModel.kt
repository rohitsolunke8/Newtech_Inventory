package com.example.newtechinventory.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newtechinventory.State
import com.example.newtechinventory.data_layer.network.responce.AddMemory
import com.example.newtechinventory.data_layer.network.responce.AddProduct
import com.example.newtechinventory.data_layer.network.responce.Categories
import com.example.newtechinventory.data_layer.network.responce.DataXXXX
import com.example.newtechinventory.data_layer.network.responce.GetMemories
import com.example.newtechinventory.data_layer.network.responce.GetProducts
import com.example.newtechinventory.data_layer.network.responce.Locations
import com.example.newtechinventory.data_layer.network.responce.MemoryDropdown
import com.example.newtechinventory.data_layer.repo.Repo
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    private val _addProductState = MutableStateFlow(AddProductState())
    val addProductState = _addProductState.asStateFlow()

    fun resetState() {
        _addProductState.update {
            AddProductState()
        }
    }

    fun addProduct(
        productName: String, categoryId: String, modelNumber: String, serialNumber: String,
//        location: String,
        description: String,

        quantity: String, warehouseName: String, aisle: String, shelf: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addProduct(
                productName = productName,
                modelNumber = modelNumber,
                serialNumber = serialNumber,
//                location = location,
                description = description,
                quantity = quantity,
                categoryId = categoryId,
                warehouseName = warehouseName,
                aisle = aisle,
                shelf = shelf,

                ).collect { state ->
                when (state) {
                    is State.Loading -> {
                        _addProductState.update {
                            AddProductState(loading = true)
                        }
                    }

                    is State.Success -> {
                        _addProductState.value = AddProductState(data = state.data, loading = false)
                    }

                    is State.Error -> {
                        _addProductState.value =
                            AddProductState(error = state.message, loading = false)
                    }
                }
            }
        }
    }


    private val _getProductState = MutableStateFlow(GetProductsState())
    val getProductsState = _getProductState.asStateFlow()

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getProducts().collectLatest { state ->
                when (state) {
                    is State.Error -> {
                        _getProductState.update {
                            GetProductsState(error = state.message, loading = false)
                        }
                    }

                    is State.Loading -> {
                        _getProductState.value = GetProductsState(loading = true)
                    }

                    is State.Success -> {
                        _getProductState.value =
                            GetProductsState(data = state.data, loading = false)
                        Log.d("view model null exception", "$state.data")
                    }
                }
            }
        }
    }

    private val _getCategoryState = MutableStateFlow(GetCategoryState())
    val getCategoryState = _getCategoryState.asStateFlow()

    private fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCategories().collectLatest { state ->
                when (state) {
                    is State.Error -> {
                        _getCategoryState.value =
                            GetCategoryState(error = state.message, loading = false)
                    }

                    is State.Loading -> {
                        _getCategoryState.value = GetCategoryState(loading = true)
                    }

                    is State.Success -> {
                        _getCategoryState.value =
                            GetCategoryState(data = state.data, loading = false)
                    }
                }
            }
        }
    }

    private val _searchedProductState = MutableStateFlow(SearchProductState())
    val searchProductState = _searchedProductState.asStateFlow()

    fun searchProduct(modelNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSearch(modelNumber = modelNumber).collectLatest { state ->
                when (state) {
                    is State.Error -> {
                        _searchedProductState.update {
                            SearchProductState(error = state.message, loading = false)
                        }
                    }

                    is State.Success<*> -> {
                        _searchedProductState.update {
                            SearchProductState(data = it.data, loading = false)
                        }
                    }

                    is State.Loading -> {
                        _searchedProductState.update {
                            SearchProductState(loading = true)
                        }
                    }
                }
            }
        }
    }

    private val _getLocationState = MutableStateFlow(GetLocationState())
    val getLocationState = _getLocationState.asStateFlow()

    private fun getLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLocations().collectLatest { state ->
                when (state) {
                    is State.Error -> {
                        _getLocationState.update {
                            GetLocationState(error = state.message, loading = false)
                        }
                    }

                    is State.Loading -> {
                        _getLocationState.update {
                            GetLocationState(loading = true)
                        }
                    }

                    is State.Success -> {
                        _getLocationState.update {
                            GetLocationState(data = state.data, loading = false)
                        }
                    }
                }
            }
        }
    }

    private val _getMemoryState = MutableStateFlow(GetMemoryState())
    val getMemoryState = _getMemoryState.asStateFlow()

    private fun getMemories() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMemories().collect { state ->
                _getMemoryState.update {
                    when (state) {
                        is State.Loading -> GetMemoryState(loading = true)
                        is State.Success -> GetMemoryState(data = state.data, loading = false)
                        is State.Error -> GetMemoryState(error = state.message, loading = false)
                    }
                }
            }
        }
    }

    private val _getSearchedMemoryState = MutableStateFlow(SearchedMemoryState())
    val getSearchedMemoryState = _getSearchedMemoryState.asStateFlow()

    fun searchedMemoryRequest(filters: Map<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchMemory(filters).collect { state ->
                _getSearchedMemoryState.update {
                    when (state) {
                        is State.Loading -> SearchedMemoryState(loading = true)
                        is State.Success -> SearchedMemoryState(
                            data = state.data, loading = false
                        )

                        is State.Error -> SearchedMemoryState(
                            error = state.message, loading = false
                        )
                    }
                }
            }
        }
    }

    private val _memoryDropdownState = MutableStateFlow(DropdownMemoryState())
    val memoryDropdownState = _memoryDropdownState.asStateFlow()

    fun memoryDropdown() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.memoryDropdowns().collectLatest { state ->
                _memoryDropdownState.update {
                    when (state) {
                        is State.Loading -> DropdownMemoryState(loading = true)
                        is State.Success -> DropdownMemoryState(
                            data = state.data, loading = false
                        )

                        is State.Error -> DropdownMemoryState(loading = true)
                    }
                }
            }
        }
    }

    private val _filteredSuggestions = MutableStateFlow<List<String>>(emptyList())
    val filteredSuggestions = _filteredSuggestions.asStateFlow()

    fun filterSuggestions(field: String, query: String) {
        viewModelScope.launch {
            val dropdownData = _memoryDropdownState.value.data?.body()
            if (dropdownData != null) {
                val suggestions = when (field) {
                    "memory_brand" -> dropdownData.memory_brands.filter {
                        it.contains(
                            query, ignoreCase = true
                        )
                    }

                    "memory_capacity" -> dropdownData.memory_capacities.filter {
                        it.contains(
                            query, ignoreCase = true
                        )
                    }

                    "memory_speed" -> dropdownData.memory_speed.filter {
                        it.contains(
                            query, ignoreCase = true
                        )
                    }

                    "memory_type" -> dropdownData.memory_types.filter {
                        it.contains(
                            query, ignoreCase = true
                        )
                    }

                    else -> emptyList()
                }
                _filteredSuggestions.emit(suggestions)
            }
        }
    }

    private val _addMemoryState = MutableStateFlow(AddMemoryState())
    val addMemorySate = _addMemoryState.asStateFlow()

    fun addMemory(
        memoryBrand: String,
        memoryType: String,
        memoryCapacity: String,
        memoryPartNumber: List<String>,
        memoryVoltage: String,
        numberOfModules: String,
        memorySpeed: String,
        memoryDescription: String,
        memorySerialNumber: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val memoryData = AddMemory(
                memory_brand = memoryBrand,
                memory_type = memoryType,
                memory_capacity = memoryCapacity,
                memory_part_number = memoryPartNumber,
                memory_voltage = memoryVoltage,
                number_of_modules = numberOfModules.toInt(),
                memory_speed = memorySpeed,
                description = memoryDescription,
                memory_serial_number = memorySerialNumber
            )
            repo.addMemory(addMemory = memoryData).collectLatest { state ->
                _addMemoryState.update {
                    when (state) {
                        is State.Loading -> AddMemoryState(loading = true)
                        is State.Success -> AddMemoryState(
                            data = state.data, loading = false
                        )

                        is State.Error -> AddMemoryState(loading = true)
                    }
                }
            }
        }
    }

    fun barcodeScanner(context: Context): GmsBarcodeScanner {
            val options = repo.scannerOptions()
            val scanner = GmsBarcodeScanning.getClient(context, options)
            return scanner
    }

    init {
        getCategories()
        getLocations()
        getMemories()
        getProducts()
        memoryDropdown()
    }


    fun resetAddProductState() {
        _addProductState.value = AddProductState()
    }
}

data class AddMemoryState(
    var loading: Boolean = false, val error: String? = null, var data: Response<AddMemory>? = null
)

data class AddProductState(
    var loading: Boolean = false, val error: String? = null, var data: Response<AddProduct>? = null
)

data class DropdownMemoryState(
    var loading: Boolean = false,
    val error: String? = null,
    var data: Response<MemoryDropdown>? = null
)

data class SearchProductState(
    var loading: Boolean = false, val error: String? = null, var data: Response<AddProduct>? = null
)

data class GetProductsState(
    val loading: Boolean = false, val error: String? = null, val data: Response<GetProducts>? = null
)

data class GetCategoryState(
    val loading: Boolean = false, val error: String? = null, val data: Response<Categories>? = null
)

data class GetLocationState(
    val loading: Boolean = false, val error: String? = null, val data: Response<Locations>? = null
)

data class GetMemoryState(
    val loading: Boolean = false, val error: String? = null, val data: Response<GetMemories>? = null
)

data class SearchedMemoryState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: Response<List<DataXXXX>>? = null
)