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
        startDate: String,
        endDate: String
    ): Result<WeatherResponse> {
        return try {
            // Open-Meteo expects start_date and end_date in yyyy-MM-dd format
            val response = weatherApi.getForecast(
                lat = lat,
                lon = lon,
                startDate = startDate,
                endDate = endDate
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSessionWeather(
        weatherResponse: WeatherResponse,
        sessionDate: String, // format: yyyy-MM-dd
        sessionTimeUtc: String // Expected format: HH:mm (UTC from Ergast)
    ): SessionWeather? {
        // Open-Meteo hourly times are in "yyyy-MM-dd'T'HH:mm"
        // We look for the hour and date that matches
        val targetHour = sessionTimeUtc.split(":")[0]
        val targetDateTimePrefix = "${sessionDate}T$targetHour"
        
        val index = weatherResponse.hourly.time.indexOfFirst { 
            it.startsWith(targetDateTimePrefix) 
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
