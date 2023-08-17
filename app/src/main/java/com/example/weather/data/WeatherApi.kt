package com.example.weather.data

import com.example.weather.entity.Weather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private const val URL = "https://api.weatherapi.com"

interface WeatherApi {
    @GET( "/v1/forecast.json?&days=5&lang=ru&key=00b13b32ddba4c0f8ff30421231508" )
    suspend fun getWeather( @Query( "q" ) location: String ) : Weather
}

val retrofit = Retrofit.Builder()
    .baseUrl( URL )
    .addConverterFactory( GsonConverterFactory.create() )
    .build()
    .create<WeatherApi>()