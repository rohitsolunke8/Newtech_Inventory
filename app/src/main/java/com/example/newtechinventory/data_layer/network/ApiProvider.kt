package com.example.newtechinventory.data_layer.network

import com.example.newtechinventory.common.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val gson = GsonBuilder()
    .setLenient()
    .create()

object ApiProvider {
    fun apiProvider(): ApiServices = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
        .create(ApiServices::class.java)
}