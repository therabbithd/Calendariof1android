package com.example.universalmotorsporttimingcalenda.ui.auth

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.universalmotorsporttimingcalenda.data.remote.model.CloudinaryConfig
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileDto
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileRequest
import com.example.universalmotorsporttimingcalenda.data.repository.AuthRepository
import com.example.universalmotorsporttimingcalenda.util.GalleryHelper
import com.example.universalmotorsporttimingcalenda.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val cloudinaryConfig: CloudinaryConfig
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Initial)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<Uri>>(emptyList())
    val galleryImages: StateFlow<List<Uri>> = _galleryImages.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    val userName: Flow<String?> = sessionManager.userName
    val userEmail: Flow<String?> = sessionManager.userEmail
    val userAvatar: Flow<String?> = sessionManager.userAvatar

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            combine(sessionManager.token, sessionManager.userId) { token, userId ->
                token to userId
            }
                .distinctUntilChanged()
                .collectLatest { (token, userId) ->
                    if (token != null) {
                        fetchProfile(token)
                    } else {
                        // Token is null, user logged out
                        _uiState.value = ProfileUiState.Initial
                    }
                }
        }
    }

    fun fetchGalleryImages(context: Context) {
        viewModelScope.launch {
            _galleryImages.value = GalleryHelper.fetchImagesFromGallery(context)
        }
    }

    fun uploadImageToCloudinary(uri: Uri, onComplete: (String?) -> Unit) {
        _isUploading.value = true
        MediaManager.get().upload(uri)
            .unsigned(cloudinaryConfig.uploadPreset)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    _isUploading.value = false
                    onComplete(url)
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    _isUploading.value = false
                    _uiState.value = ProfileUiState.Error("Upload failed: ${error.description}")
                    onComplete(null)
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    _isUploading.value = false
                    onComplete(null)
                }
            }).dispatch()
    }

    private suspend fun fetchProfile(token: String) {
        _uiState.value = ProfileUiState.Loading
        
        repository.getProfile(token).collect { result ->
            if (result.isSuccess) {
                val profile = result.getOrNull()
                
                if (profile != null) {
                    val finalName = profile.user?.name ?: sessionManager.userName.first()
                    val finalEmail = profile.user?.email ?: sessionManager.userEmail.first()
                    val finalAvatar = profile.avatar ?: profile.user?.avatar ?: sessionManager.userAvatar.first()
                    
                    sessionManager.updateUserName(finalName)
                    sessionManager.updateUserEmail(finalEmail)
                    sessionManager.updateUserAvatar(finalAvatar)
                    
                    _uiState.value = ProfileUiState.Success(profile)
                } else {
                    _uiState.value = ProfileUiState.NoProfile
                }
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                if (errorMessage.contains("perfil no encontrado", ignoreCase = true)) {
                    _uiState.value = ProfileUiState.NoProfile
                } else {
                    _uiState.value = ProfileUiState.Error(errorMessage)
                }
            }
        }
    }

    fun createProfile(bio: String, phone: String, address: String, avatar: String) {
        viewModelScope.launch {
            val token = sessionManager.token.first() ?: return@launch
            val request = ProfileRequest(
                bio = bio,
                phone = phone,
                address = address,
                avatar = avatar,
                configuracion = "",
                favoritos = ""
            )

            _uiState.value = ProfileUiState.Loading
            repository.createProfile(token, request).collect { result ->
                if (result.isSuccess) {
                    val profile = result.getOrNull()
                    if (profile != null) {
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
            viewModelScope.launch {
                val token = sessionManager.token.first()
                if (token != null) {
                    fetchProfile(token)
                }
            }
        }
    }
}

sealed class ProfileUiState {
    object Initial : ProfileUiState()
    object Loading : ProfileUiState()
    object NoProfile : ProfileUiState()
    object CreationSuccess : ProfileUiState()
    data class Success(val profile: ProfileDto) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}
