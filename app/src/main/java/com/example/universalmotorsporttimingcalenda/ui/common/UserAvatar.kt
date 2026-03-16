package com.example.universalmotorsporttimingcalenda.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun UserAvatar(
    userName: String?,
    avatarSource: Any?, // Can be Uri, String, etc.
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    showPlaceholder: Boolean = true
) {
    val encodedName = URLEncoder.encode(userName ?: "U", StandardCharsets.UTF_8.toString())
    val fallbackUrl = "https://ui-avatars.com/api/?name=$encodedName&background=random&color=fff&size=256"
    
    val finalSource = avatarSource ?: if (showPlaceholder) fallbackUrl else null

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val model = ImageRequest.Builder(LocalContext.current)
            .data(finalSource)
            .crossfade(true)
            .build()

        AsyncImage(
            model = model,
            contentDescription = "Profile Picture",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            error = if (showPlaceholder && finalSource != fallbackUrl) {
                // If the primary image fails, we can't easily call another AsyncImage here
                // because error is a Painter, not a Composable. 
                // However, we already set finalSource to fallbackUrl if avatarSource is null.
                // If avatarSource IS provided but FAILS, Coil will show the error painter.
                // For now, let's just use the fallbackUrl as the data source if avatarSource is null,
                // and if it fails, it will show nothing or a default error painter.
                null 
            } else null
        )
    }
}
