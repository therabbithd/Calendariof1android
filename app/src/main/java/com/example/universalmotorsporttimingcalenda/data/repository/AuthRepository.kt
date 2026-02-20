package com.example.universalmotorsporttimingcalenda.data.repository

import com.example.universalmotorsporttimingcalenda.data.remote.AuthApi
import com.example.universalmotorsporttimingcalenda.data.remote.model.AuthResponse
import com.example.universalmotorsporttimingcalenda.data.remote.model.LoginRequest
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileDto
import com.example.universalmotorsporttimingcalenda.data.remote.model.RegisterRequest
import com.example.universalmotorsporttimingcalenda.data.remote.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<AuthResponse>>
    fun register(email: String, name: String, password: String): Flow<Result<AuthResponse>>
    fun getMe(token: String): Flow<Result<ProfileDto>>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Result<AuthResponse>> = flow {
        try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.errorBody()?.string() ?: "Login failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun register(email: String, name: String, password: String): Flow<Result<AuthResponse>> = flow {
        try {
            val response = api.register(RegisterRequest(email, name, password))
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.errorBody()?.string() ?: "Registration failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getMe(token: String): Flow<Result<ProfileDto>> = flow {
        try {
            val response = api.getMe("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception(response.errorBody()?.string() ?: "Failed to fetch user")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
