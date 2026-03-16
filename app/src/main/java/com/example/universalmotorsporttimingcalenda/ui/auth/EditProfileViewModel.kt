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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val cloudinaryConfig: CloudinaryConfig
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditProfileUiState>(EditProfileUiState.Initial)
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _isUploading = MutableStateFlow(false)
    val isUploading: StateFlow<Boolean> = _isUploading.asStateFlow()

    private val _galleryImages = MutableStateFlow<List<Uri>>(emptyList())
    val galleryImages: StateFlow<List<Uri>> = _galleryImages.asStateFlow()

    private val _uploadedAvatarUrl = MutableStateFlow<String?>(null)
    val uploadedAvatarUrl: StateFlow<String?> = _uploadedAvatarUrl.asStateFlow()

    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()

    init {
        loadCurrentProfile()
    }

    private fun loadCurrentProfile() {
        viewModelScope.launch {
            val token = sessionManager.token.first()
            if (token != null) {
                _uiState.value = EditProfileUiState.Loading
                repository.getProfile(token).collect { result ->
                    if (result.isSuccess) {
                        val profile = result.getOrNull()
                        if (profile != null) {
                            _uiState.value = EditProfileUiState.Success(profile)
                        } else {
                            _uiState.value = EditProfileUiState.Error("Profile data not found")
                        }
                    } else {
                        val rawError = result.exceptionOrNull()?.message ?: "Unknown error"
                        _uiState.value = EditProfileUiState.Error(com.example.universalmotorsporttimingcalenda.util.ErrorHandler.parseError(rawError))
                    }
                }
            } else {
                _uiState.value = EditProfileUiState.Error("Not authenticated")
            }
        }
    }

    fun fetchGalleryImages(context: Context) {
        viewModelScope.launch {
            _galleryImages.value = GalleryHelper.fetchImagesFromGallery(context)
        }
    }

    fun uploadImage(uri: Uri) {
        _isUploading.value = true
        _uploadProgress.value = 0f
        MediaManager.get().upload(uri)
            .unsigned(cloudinaryConfig.uploadPreset)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    _uploadProgress.value = bytes.toFloat() / totalBytes.toFloat()
                }
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    _isUploading.value = false
                    _uploadedAvatarUrl.value = url
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    _isUploading.value = false
                    val rawError = "Upload failed: ${error.description}"
                    _uiState.value = EditProfileUiState.Error(com.example.universalmotorsporttimingcalenda.util.ErrorHandler.parseError(rawError))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    _isUploading.value = false
                }
            }).dispatch()
    }

    fun updateProfile(bio: String, phone: String, address: String, avatar: String) {
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

            _uiState.value = EditProfileUiState.Loading
            repository.updateProfile(token, request).collect { result ->
                if (result.isSuccess) {
                    val profile = result.getOrNull()
                    if (profile != null) {
                        // Update session manager with new info
                        sessionManager.updateUserAvatar(avatar)
                        _uiState.value = EditProfileUiState.UpdateSuccess
                    } else {
                        _uiState.value = EditProfileUiState.Error("Failed to parse update response")
                    }
                } else {
                    val rawError = result.exceptionOrNull()?.message ?: "Update failed"
                    _uiState.value = EditProfileUiState.Error(com.example.universalmotorsporttimingcalenda.util.ErrorHandler.parseError(rawError))
                }
            }
        }
    }
}

sealed class EditProfileUiState {
    object Initial : EditProfileUiState()
    object Loading : EditProfileUiState()
    object UpdateSuccess : EditProfileUiState()
    data class Success(val profile: ProfileDto) : EditProfileUiState()
    data class Error(val message: String) : EditProfileUiState()
}
