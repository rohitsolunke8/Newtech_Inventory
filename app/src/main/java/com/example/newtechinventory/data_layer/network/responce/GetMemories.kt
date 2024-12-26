package com.example.newtechinventory.data_layer.network.responce

import kotlinx.serialization.Serializable

@Serializable
data class GetMemories(
    val `data`: List<DataXXXX>,
    val status: Int
)