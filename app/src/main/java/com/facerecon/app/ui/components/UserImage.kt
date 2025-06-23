package com.facerecon.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.facerecon.app.R
import com.facerecon.app.utils.NetworkConfig

@Composable
fun UserImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    showPlaceholder: Boolean = true
) {
    val context = LocalContext.current
    
    if (imageUrl.isNullOrBlank()) {
        if (showPlaceholder) {
            // Show placeholder when no image URL
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person_placeholder),
                    contentDescription = contentDescription ?: "Sin imagen",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        // Build complete image URL
        val completeImageUrl = NetworkConfig.buildImageUrl(imageUrl)
        
        if (completeImageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(completeImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription ?: "Imagen de usuario",
                modifier = modifier.clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = {
                    // Handle error by showing placeholder
                    if (showPlaceholder) {
                        // This will be handled by the error state
                    }
                }
            )
        } else {
            // Show placeholder if URL building failed
            if (showPlaceholder) {
                Box(
                    modifier = modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person_placeholder),
                        contentDescription = contentDescription ?: "Error al cargar imagen",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun UserImageWithFallback(
    imageUrl: String?,
    userName: String,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    val context = LocalContext.current
    
    if (imageUrl.isNullOrBlank()) {
        // Show initials when no image
        Box(
            modifier = modifier
                .size(size.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getInitials(userName),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    } else {
        // Try to load image with fallback
        val completeImageUrl = NetworkConfig.buildImageUrl(imageUrl)
        
        if (completeImageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(completeImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de $userName",
                modifier = modifier
                    .size(size.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                onError = {
                    // Show initials on error
                    Box(
                        modifier = modifier
                            .size(size.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getInitials(userName),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        } else {
            // Show initials if URL building failed
            Box(
                modifier = modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(userName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Extracts initials from a full name
 */
private fun getInitials(fullName: String): String {
    return fullName.split(" ")
        .take(2)
        .map { it.firstOrNull()?.uppercase() ?: "" }
        .joinToString("")
        .take(2)
} 