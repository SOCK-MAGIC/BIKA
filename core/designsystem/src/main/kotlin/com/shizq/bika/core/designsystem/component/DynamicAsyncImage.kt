package com.shizq.bika.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State.Loading
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.shizq.bika.core.designsystem.R
import com.shizq.bika.core.designsystem.theme.LocalTintTheme

/**
 * A wrapper around [AsyncImage] which determines the colorFilter based on the theme
 */
@Composable
fun DynamicAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter = painterResource(R.drawable.core_designsystem_ic_placeholder_default),
) {
    val iconTint = LocalTintTheme.current.iconTint
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val context = LocalPlatformContext.current
    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .diskCacheKey(imageUrl.substringAfterLast("/"))
            .build(),
        onState = { state ->
            isLoading = state is Loading
            isError = state is Error
        },
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading && !isLocalInspection) {
            // Display a progress bar while loading
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
        Image(
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            painter = if (isError.not() && !isLocalInspection) imageLoader else placeholder,
            contentDescription = contentDescription,
            colorFilter = if (iconTint != Unspecified) ColorFilter.tint(iconTint) else null,
        )
    }
}

@Composable
fun DynamicAsyncImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun AvatarAsyncImage(
    avatarUrl: String,
    avatarBorderUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier.size(64.dp), contentAlignment = Alignment.Center) {
        AsyncImage(
            avatarUrl,
            "Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
        )
        AsyncImage(
            avatarBorderUrl,
            "Avatar Border",
            Modifier.fillMaxSize(),
        )
    }
}
