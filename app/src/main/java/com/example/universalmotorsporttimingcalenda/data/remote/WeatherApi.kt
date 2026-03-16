package com.example.universalmotorsporttimingcalenda.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,weathercode",
        @Query("timezone") timezone: String = "UTC",
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): WeatherResponse
}

data class WeatherResponse(
    val hourly: HourlyData
)

data class HourlyData(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weathercode: List<Int>
)
