package com.example.newtechinventory.data_layer.network.responce

import kotlinx.serialization.Serializable

@Serializable
data class DataXXXX(
    val created_at: String,
    val description: String,
    val memory_brand: String,
    val memory_capacity: String,
    val memory_id: Int ?= null,
    val memory_part_number: String,
    val memory_serial_number: String,
    val memory_speed: String,
    val memory_type: String,
    val memory_voltage: String,
    val number_of_modules: Int,
    val status: String,
    val updated_at: String
)