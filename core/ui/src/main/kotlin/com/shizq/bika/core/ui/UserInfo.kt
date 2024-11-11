package com.shizq.bika.core.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.asImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

data class UserInfo(
    val avatarUrl: String,
    val gender: String,
    val level: String,
    val username: String,
    val slogan: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDialog(
    user: UserInfo,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            modifier = Modifier.clickable { },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp),
            ) {
                DynamicAsyncImage(
                    user.avatarUrl,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                )
                Text("${user.gender} Lv.${user.level}",)
                Text(user.username, fontWeight = FontWeight.Bold)
                Text(user.slogan)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
private fun UserDialogPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorDrawable(android.graphics.Color.RED).asImage()
    }
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        UserDialog(
            UserInfo(
                avatarUrl = "",
                gender = "男",
                level = "1",
                username = "shizq",
                slogan = "slogan",
            ),
            onDismiss = {},
        )
    }
}