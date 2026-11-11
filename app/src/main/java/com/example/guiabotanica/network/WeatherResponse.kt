package com.example.guiabotanica.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current: CurrentUnits?
)

data class CurrentUnits(
    val time: String,
    @SerializedName("temperature_2m") val temperature2m: Double,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double
)