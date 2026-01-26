package com.example.universalmotorsporttimingcalenda.data.remote

import com.example.universalmotorsporttimingcalenda.data.remote.model.ErgastResponse
import retrofit2.Response
import retrofit2.http.GET

interface F1Api {

    @GET("/ergast/f1/2026.json")
    suspend fun getSeason2026(): Response<ErgastResponse>
}
