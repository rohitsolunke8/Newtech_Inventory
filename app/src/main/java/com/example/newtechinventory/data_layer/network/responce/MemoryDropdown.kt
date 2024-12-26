package com.example.newtechinventory.data_layer.network.responce

data class MemoryDropdown(
    val memory_brands: List<String>,
    val memory_capacities: List<String>,
    val memory_modules: List<Int>,
    val memory_part_number: List<String>,
    val memory_speed: List<String>,
    val memory_types: List<String>,
    val memory_voltage: List<String>
)