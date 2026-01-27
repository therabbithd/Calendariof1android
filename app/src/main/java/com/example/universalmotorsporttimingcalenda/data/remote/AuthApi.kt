package com.example.universalmotorsporttimingcalenda.data.remote

import com.example.universalmotorsporttimingcalenda.data.remote.model.AuthResponse
import com.example.universalmotorsporttimingcalenda.data.remote.model.LoginRequest
import com.example.universalmotorsporttimingcalenda.data.remote.model.RegisterRequest
import com.example.universalmotorsporttimingcalenda.data.remote.model.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/api/users/me")
    suspend fun getMe(@Header("Authorization") token: String): Response<UserDto>
}
