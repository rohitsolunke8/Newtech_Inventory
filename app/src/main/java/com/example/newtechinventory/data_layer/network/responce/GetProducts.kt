package com.example.newtechinventory.data_layer.network.responce

data class GetProducts(
    val `data`: List<Data>,
    val status: Int
)