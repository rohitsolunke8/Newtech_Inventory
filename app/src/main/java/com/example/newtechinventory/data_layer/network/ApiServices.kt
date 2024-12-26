package com.example.newtechinventory.data_layer.network

import com.example.newtechinventory.data_layer.network.responce.AddMemory
import com.example.newtechinventory.data_layer.network.responce.AddProduct
import com.example.newtechinventory.data_layer.network.responce.Categories
import com.example.newtechinventory.data_layer.network.responce.DataXXXX
import com.example.newtechinventory.data_layer.network.responce.GetMemories
import com.example.newtechinventory.data_layer.network.responce.GetProducts
import com.example.newtechinventory.data_layer.network.responce.Locations
import com.example.newtechinventory.data_layer.network.responce.MemoryDropdown
import com.example.newtechinventory.data_layer.network.responce.SearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @FormUrlEncoded
    @POST("addproduct")
    suspend fun addProduct(
        @Field("product_name") productName: String,
        @Field("category_id") categoryId: String,
        @Field("model_number") modelNumber: String,
        @Field("serial_number") serialNumber: String,
//        @Field("location_id") location: String,
        @Field("warehouse_name") warehouseName: String,
        @Field("aisle") aisle: String,
        @Field("shelf") shelf: String,
        @Field("quantity_in_stock") quantity: String,
        @Field("description") description: String,
    ): Response<AddProduct>

    @GET("products")
    suspend fun getProducts(): Response<GetProducts>

    @GET("categories")
    suspend fun getCategories(): Response<Categories>

    @POST("search")
    suspend fun searchProduct(
        @Field("model_number")modelNumber: String
    ): Response<SearchResponse>

    @GET("locations")
    suspend fun getLocations(): Response<Locations>

    @GET("memories")
    suspend fun getMemories(): Response<GetMemories>

    @POST("search_memory")
    suspend fun searchMemories(@Body filters: Map<String, String>): Response<List<DataXXXX>>

    @GET("memory_drop_down_options")
    suspend fun memoryDropdowns(): Response<MemoryDropdown>

    @POST("add_memory")
    suspend fun addMemory(@Body memoryData: AddMemory): Response<AddMemory>
}