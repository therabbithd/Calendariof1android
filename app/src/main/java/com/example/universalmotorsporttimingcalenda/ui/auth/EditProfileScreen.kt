package com.example.universalmotorsporttimingcalenda.ui.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.universalmotorsporttimingcalenda.data.remote.model.ProfileDto
import com.example.universalmotorsporttimingcalenda.ui.common.UserAvatar
import com.example.universalmotorsporttimingcalenda.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    onUpdateSuccess: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    val uploadProgress by viewModel.uploadProgress.collectAsStateWithLifecycle()
    val uploadedAvatarUrl by viewModel.uploadedAvatarUrl.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState is EditProfileUiState.UpdateSuccess) {
            onUpdateSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.edit_profile)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is EditProfileUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EditProfileUiState.Success -> {
                    val profile = (uiState as EditProfileUiState.Success).profile
                    val userName = profile.user?.name
                    val encodedName = java.net.URLEncoder.encode(userName ?: "U", "UTF-8")
                    val fallbackAvatarUrl = "https://ui-avatars.com/api/?name=$encodedName&background=random&color=fff&size=256"
                    EditProfileContent(
                        profile = profile,
                        isUploading = isUploading,
                        uploadProgress = uploadProgress,
                        uploadedAvatarUrl = uploadedAvatarUrl,
                        onImageSelected = { uri -> viewModel.uploadImage(uri) },
                        onSave = { bio, phone, address, uri ->
                            val avatarToSave = uploadedAvatarUrl
                                ?: profile.avatar?.ifEmpty { fallbackAvatarUrl }
                                ?: fallbackAvatarUrl
                            viewModel.updateProfile(bio, phone, address, avatarToSave)
                        }
                    )
                }
                is EditProfileUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as EditProfileUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { /* Could add retry here */ }) {
                            Text(stringResource(id = R.string.retry))
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun EditProfileContent(
    profile: ProfileDto,
    isUploading: Boolean,
    uploadProgress: Float,
    uploadedAvatarUrl: String?,
    onImageSelected: (Uri) -> Unit,
    onSave: (String, String, String, Uri?) -> Unit
) {
    var bio by remember { mutableStateOf(profile.bio ?: "") }
    var phone by remember { mutableStateOf(profile.phone ?: "") }
    var address by remember { mutableStateOf(profile.address ?: "") }
    // var favoritos by remember { mutableStateOf(profile.favoritos ?: "") } // Removed
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            onImageSelected(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable { launcher.launch("image/*") }
        ) {
            UserAvatar(
                userName = profile.user?.name,
                avatarSource = selectedImageUri ?: profile.avatar ?: profile.user?.avatar,
                size = 120.dp
            )
            
            if (isUploading) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { uploadProgress },
                            color = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape),
                tonalElevation = 4.dp
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = stringResource(id = R.string.change_picture),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text(stringResource(id = R.string.biography)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(id = R.string.phone_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(id = R.string.address_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Favorites field removed

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onSave(bio, phone, address, selectedImageUri) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            enabled = !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.save_changes), fontWeight = FontWeight.Bold)
            }
        }
    }
}
