package com.example.universalmotorsporttimingcalenda.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileDto
import com.example.universalmotorsporttimingcalenda.data.remote.model.UserDto
import com.example.universalmotorsporttimingcalenda.data.repository.AuthRepository
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val userName: String? get() = sessionManager.userName
    val userEmail: String? get() = sessionManager.userEmail
    val userAvatar: String? get() = sessionManager.userAvatar

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        val token = sessionManager.token
        if (token == null) {
            _uiState.value = ProfileUiState.Error("No session found")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            repository.getMe(token).collect { result ->
                if (result.isSuccess) {
                    val profile = result.getOrNull()
                    if (profile != null) {
                        // Robust data mapping: check root profile fields and nested user fields
                        val finalName = profile.user?.name ?: sessionManager.userName
                        val finalEmail = profile.user?.email ?: sessionManager.userEmail
                        val finalAvatar = profile.avatar ?: profile.user?.avatar ?: sessionManager.userAvatar
                        
                        sessionManager.userName = finalName
                        sessionManager.userEmail = finalEmail
                        sessionManager.userAvatar = finalAvatar
                        
                        _uiState.value = ProfileUiState.Success(profile)
                    } else {
                        _uiState.value = ProfileUiState.Error("Profile data is empty")
                    }
                } else {
                    _uiState.value = ProfileUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        }
    }
}

sealed class ProfileUiState {
    object Initial : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val profile: ProfileDto) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
