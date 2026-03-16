package com.example.universalmotorsporttimingcalenda.data.repository

import com.example.universalmotorsporttimingcalenda.data.remote.WeatherApi
import com.example.universalmotorsporttimingcalenda.data.remote.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton
import java.text.SimpleDateFormat
import java.util.*

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {
    suspend fun getWeatherData(
        lat: Double,
        lon: Double,
        date: String
    ): Result<WeatherResponse> {
        return try {
            // Open-Meteo expects start_date and end_date in yyyy-MM-dd format
            val response = weatherApi.getForecast(
                lat = lat,
                lon = lon,
                startDate = date,
                endDate = date
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSessionWeather(
        weatherResponse: WeatherResponse,
        sessionTimeUtc: String // Expected format: HH:mm (UTC from Ergast)
    ): SessionWeather? {
        // Open-Meteo hourly times are in "yyyy-MM-dd'T'HH:mm"
        // We look for the hour that matches sessionTimeUtc
        val targetHour = sessionTimeUtc.split(":")[0]
        
        val index = weatherResponse.hourly.time.indexOfFirst { 
            it.split("T")[1].startsWith(targetHour) 
        }

        return if (index != -1) {
            SessionWeather(
                temperature = weatherResponse.hourly.temperature_2m[index],
                weatherCode = weatherResponse.hourly.weathercode[index]
            )
        } else null
    }
}

data class SessionWeather(
    val temperature: Double,
    val weatherCode: Int
)
