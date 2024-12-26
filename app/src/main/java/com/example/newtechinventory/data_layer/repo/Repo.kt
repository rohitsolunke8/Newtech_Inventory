package com.example.newtechinventory.data_layer.repo

import android.util.Log
import com.example.newtechinventory.State
import com.example.newtechinventory.data_layer.network.ApiProvider
import com.example.newtechinventory.data_layer.network.responce.AddMemory
import com.example.newtechinventory.data_layer.network.responce.AddProduct
import com.example.newtechinventory.data_layer.network.responce.Categories
import com.example.newtechinventory.data_layer.network.responce.DataXXXX
import com.example.newtechinventory.data_layer.network.responce.GetMemories
import com.example.newtechinventory.data_layer.network.responce.GetProducts
import com.example.newtechinventory.data_layer.network.responce.Locations
import com.example.newtechinventory.data_layer.network.responce.MemoryDropdown
import com.example.newtechinventory.data_layer.network.responce.SearchResponse
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repo {
    fun addProduct(
        productName: String,
        categoryId: String,
        modelNumber: String,
        serialNumber: String,
//        location: String,
        quantity: String,
        description: String,
        warehouseName: String,
        aisle: String,
        shelf: String
    ): Flow<State<Response<AddProduct>>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.apiProvider().addProduct(
                productName = productName,
                categoryId = categoryId.toString(),
                modelNumber = modelNumber,
                serialNumber = serialNumber,
//                location = location,
                quantity = quantity,
                description = description,
                warehouseName = warehouseName,
                aisle = aisle,
                shelf = shelf,
            )

            emit(State.Success(response))
            Log.d("response", "${response.body()} - ${response.code()} - ${response.message()}")

        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getProducts(): Flow<State<Response<GetProducts>>> = flow {
        try {
            val response = ApiProvider.apiProvider().getProducts()
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getCategories(): Flow<State<Response<Categories>>> = flow {
        try {
            val response = ApiProvider.apiProvider().getCategories()
            Log.d("repo null exception", "$response")

            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getSearch(modelNumber: String): Flow<State<Response<SearchResponse>>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.apiProvider().searchProduct(
                modelNumber = modelNumber
            )
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getLocations(): Flow<State<Response<Locations>>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.apiProvider().getLocations()
            emit(State.Success(response))
            Log.d("response", "$response")
        } catch (e: Exception) {
            emit(State.Error(e.message.toString()))
        }
    }

    fun getMemories(): Flow<State<Response<GetMemories>>> = flow {
        emit(State.Loading)
        try {
            val response = ApiProvider.apiProvider().getMemories()
            if (response.isSuccessful) {
                emit(State.Success(response))
                Log.d("API Response", "Response: ${response.body()?.data}")
            } else {
                emit(State.Error("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            emit(State.Error("Exception: ${e.message}"))
        }
    }

    fun searchMemory(filters: Map<String, String>): Flow<State<Response<List<DataXXXX>>>> = flow {
        try {
            val response = ApiProvider.apiProvider().searchMemories(filters)
            if (response.isSuccessful) {
                emit(State.Success(response))
                Log.d("Search Response", "Response: ${response.body()}")
            } else {
                emit(State.Error("Error: ${response.errorBody()?.toString()}"))
                Log.d("Search Response", "Response: ${response.body()}")
            }
        } catch (e: Exception) {
            Log.d("Search Response", "Response: ${e}")
            emit(State.Error("Exception: ${e.message}"))
        }
    }

    fun memoryDropdowns(): Flow<State<Response<MemoryDropdown>>> = flow {
        try {
            val response = ApiProvider.apiProvider().memoryDropdowns()
            if (response.isSuccessful){
                emit(State.Success(response))
                Log.d("Dropdown Response", "Response: ${response.body()}")
            } else {
                emit(State.Error("Error: ${response.errorBody()}"))
                Log.d("Dropdown Response", "Response: ${response.body()}")
            }
        } catch (e: Exception) {
            emit(State.Error("Exception: ${e.message}"))
            Log.d("Dropdown Response", "Response: $e")
        }
    }

    fun addMemory(addMemory: AddMemory): Flow<State<Response<AddMemory>>> = flow {
        try {
            val response = ApiProvider.apiProvider().addMemory(
                memoryData = addMemory
            )
            if (response.isSuccessful) {
                Log.d("AddMemoryResponse", "Response: ${response.body()}")
                emit(State.Success(response))
            } else {
                emit(State.Error("AddMemoryResponse: ${response.errorBody()}"))
            }
        } catch (e: Exception) {
            emit(State.Error("AddMemoryResponse: ${e.message}"))
        }
    }


    fun scannerOptions(): GmsBarcodeScannerOptions {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_CODABAR
            )
            .enableAutoZoom()
            .build()
        return options
    }
}