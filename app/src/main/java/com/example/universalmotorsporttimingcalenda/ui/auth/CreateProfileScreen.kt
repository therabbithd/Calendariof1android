package com.example.universalmotorsporttimingcalenda.ui.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import android.Manifest
import android.os.Build
import coil3.compose.AsyncImage
import com.example.universalmotorsporttimingcalenda.R
import com.example.universalmotorsporttimingcalenda.ui.common.UserAvatar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onCreateSuccess: () -> Unit,
    onSkip: () -> Unit
) {
    var bio by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var showGallery by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsState()
    val galleryImages by viewModel.galleryImages.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val currentUserName by viewModel.userName.collectAsState(initial = null)
    
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val storagePermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    LaunchedEffect(uiState) {
        when (uiState) {
            is ProfileUiState.CreationSuccess -> {
                onCreateSuccess()
            }
            is ProfileUiState.Error -> {
                Toast.makeText(context, (uiState as ProfileUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    if (showGallery) {
        AlertDialog(
            onDismissRequest = { showGallery = false },
            confirmButton = {},
            title = { Text(stringResource(id = R.string.select_image)) },
            text = {
                Box(modifier = Modifier.height(400.dp)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(galleryImages) { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clickable {
                                        viewModel.uploadImageToCloudinary(uri) { url ->
                                            if (url != null) {
                                                avatarUrl = url
                                            }
                                            showGallery = false
                                        }
                                    },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.create_profile), style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Avatar Preview / Selector
        Box(
            modifier = Modifier
                .size(120.dp)
                .clickable {
                    if (storagePermissionState.status.isGranted) {
                        viewModel.fetchGalleryImages(context)
                        showGallery = true
                    } else {
                        storagePermissionState.launchPermissionRequest()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val currentUserName by viewModel.userName.collectAsState(initial = null)
            UserAvatar(
                userName = currentUserName,
                avatarSource = avatarUrl.ifEmpty { null },
                size = 120.dp
            )
            
            if (avatarUrl.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Select Photo",
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }
            
            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text(stringResource(id = R.string.bio_label)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(id = R.string.phone_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(id = R.string.address_label)) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val encodedName = java.net.URLEncoder.encode(currentUserName ?: "U", "UTF-8")
                val fallbackAvatar = "https://ui-avatars.com/api/?name=$encodedName&background=random&color=fff&size=256"
                viewModel.createProfile(bio, phone, address, avatarUrl.ifEmpty { fallbackAvatar })
            },
            enabled = uiState !is ProfileUiState.Loading && !isUploading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is ProfileUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(id = R.string.submit_profile))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.skip_profile))
        }
    }
}
