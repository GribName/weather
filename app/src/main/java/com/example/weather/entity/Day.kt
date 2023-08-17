package com.example.weather.entity

data class Day(
    val avghumidity: Double,
    val avgtemp_c: Double,
    val condition: Condition,
    val maxwind_kph: Double,
)