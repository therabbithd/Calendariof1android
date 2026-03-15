package com.example.universalmotorsporttimingcalenda.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileDto
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileRequest
import com.example.universalmotorsporttimingcalenda.data.remote.model.UserDto
import com.example.universalmotorsporttimingcalenda.data.repository.AuthRepository
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    val userName: Flow<String?> = sessionManager.userName
    val userEmail: Flow<String?> = sessionManager.userEmail
    val userAvatar: Flow<String?> = sessionManager.userAvatar

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            val token = sessionManager.token.first()
            if (token == null) {
                _uiState.value = ProfileUiState.Error("No session found")
                return@launch
            }

            _uiState.value = ProfileUiState.Loading
            repository.getProfile(token).collect { result ->
                if (result.isSuccess) {
                    val profile = result.getOrNull()
                    if (profile != null) {
                        // Robust data mapping: check root profile fields and nested user fields
                        val currentName = sessionManager.userName.first()
                        val currentEmail = sessionManager.userEmail.first()
                        val currentAvatar = sessionManager.userAvatar.first()

                        val finalName = profile.user?.name ?: currentName
                        val finalEmail = profile.user?.email ?: currentEmail
                        val finalAvatar = profile.avatar ?: profile.user?.avatar ?: currentAvatar
                        
                        sessionManager.updateUserName(finalName)
                        sessionManager.updateUserEmail(finalEmail)
                        sessionManager.updateUserAvatar(finalAvatar)
                        
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

    fun createProfile(bio: String, phone: String, address: String, avatar: String) {
        viewModelScope.launch {
            val token = sessionManager.token.first() ?: return@launch
            val request = ProfileRequest(bio, phone, address, avatar)

            _uiState.value = ProfileUiState.Loading
            repository.createProfile(token, request).collect { result ->
                if (result.isSuccess) {
                    val profile = result.getOrNull()
                    if (profile != null) {
                        // Update session manager with new avatar if it was part of the profile
                        sessionManager.updateUserAvatar(profile.avatar ?: profile.user?.avatar ?: avatar)
                        _uiState.value = ProfileUiState.CreationSuccess
                    } else {
                        _uiState.value = ProfileUiState.Error("Failed to parse profile response")
                    }
                } else {
                    _uiState.value = ProfileUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
            }
        }
    }

    fun resetState() {
        if (_uiState.value is ProfileUiState.Error || _uiState.value is ProfileUiState.CreationSuccess) {
            fetchProfile() // Refresh or go back to initial
        }
    }
}

sealed class ProfileUiState {
    object Initial : ProfileUiState()
    object Loading : ProfileUiState()
    object CreationSuccess : ProfileUiState()
    data class Success(val profile: ProfileDto) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
