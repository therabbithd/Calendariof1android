package com.example.universalmotorsporttimingcalenda.data.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("user") val user: UserDto,
    @SerializedName("token") val token: String,
    @SerializedName("avatar") val avatar: String? = null
)

data class ProfileDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("userId") val userId: Int?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("user") val user: UserInsideProfileDto?
)

data class UserInsideProfileDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("bio") val bio: String? = null
)

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class ErrorResponse(
    @SerializedName("message") val message: String
)
