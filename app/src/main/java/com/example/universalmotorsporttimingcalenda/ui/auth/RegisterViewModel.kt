package com.example.universalmotorsporttimingcalenda.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.repository.AuthRepository
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(email: String, name: String, password: String) {
        if (email.isBlank() || name.isBlank() || password.isBlank()) {
            _uiState.value = RegisterUiState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            repository.register(email, name, password).collect { result ->
                if (result.isSuccess) {
                    val authResponse = result.getOrNull()
                    if (authResponse != null) {
                        sessionManager.saveSession(
                            token = authResponse.token,
                            name = authResponse.user.name,
                            email = authResponse.user.email,
                            avatar = authResponse.avatar
                        )
                        _uiState.value = RegisterUiState.Success
                    } else {
                        _uiState.value = RegisterUiState.Error("Registration response was empty")
                    }
                } else {
                    _uiState.value = RegisterUiState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState.Initial
    }
}

sealed class RegisterUiState {
    object Initial : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
