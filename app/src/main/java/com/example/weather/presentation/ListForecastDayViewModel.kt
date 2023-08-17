package com.example.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.retrofit
import com.example.weather.entity.Forecastday
import com.example.weather.entity.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListForecastDayViewModel : ViewModel() {

    private val _listForecastDay = MutableStateFlow<List<Forecastday>>( emptyList() )
    val listForecastDay = _listForecastDay.asStateFlow()

    private val _locationName = MutableStateFlow<Location>( Location( "Определение местоположения" ) )
    val locationName = _locationName.asStateFlow()

    /*init {
        loadWeather()
    }*/

    fun loadWeather( location: String ) {
        viewModelScope.launch( Dispatchers.IO ) {
            kotlin.runCatching {
                retrofit.getWeather( location )
            }.fold(
                onSuccess = {
                    _locationName.value = it.location
                    _listForecastDay.value = it.forecast.forecastday
                            },
                onFailure = { throw Exception( "Error" ) }
            )
        }
    }
}