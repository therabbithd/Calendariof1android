package com.example.universalmotorsporttimingcalenda.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.universalmotorsporttimingcalenda.util.SessionManager

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            repository.login(email, password).collect { result ->
                if (result.isSuccess) {
                    val response = result.getOrNull()
                    sessionManager.token = response?.token
                    sessionManager.userName = response?.user?.name
                    sessionManager.userEmail = response?.user?.email
                    // Check for avatar in both user object and root response
                    sessionManager.userAvatar = response?.user?.avatar ?: response?.avatar
                    _uiState.value = LoginUiState.Success(response?.token ?: "")
                } else {
                    _uiState.value = LoginUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class LoginUiState {
    object Initial : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
