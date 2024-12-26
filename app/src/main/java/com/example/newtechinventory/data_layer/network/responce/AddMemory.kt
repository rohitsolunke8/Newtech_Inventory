package com.example.newtechinventory.data_layer.network.responce

data class AddMemory(
    val description: String,
    val memory_brand: String,
    val memory_capacity: String,
    val memory_part_number: List<String>,
    val memory_serial_number: List<String>,
    val memory_speed: String,
    val memory_type: String,
    val memory_voltage: String,
    val number_of_modules: Int
)
