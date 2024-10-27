package com.shizq.bika.feature.comic.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun UserDialog(
    avatarUrl: String,
    gender: String,
    level: String,
    username: String,
    slogan: String,
    onDismiss: () -> Unit,
) {
    var show by remember { mutableStateOf(false) }
    if (false) {
        Pop(avatarUrl) { show = false }
    }
    BasicAlertDialog(onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            modifier = Modifier.clickable { show = true },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp),
            ) {
                DynamicAsyncImage(
                    avatarUrl,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                )
                Text(
                    "$gender Lv.$level",
                )
                Text(username, fontWeight = FontWeight.Bold)
                Text(slogan)
            }
        }
    }
}

@Composable
fun Pop(avatarUrl: String, onDismiss: () -> Unit) {
    Popup(alignment = Alignment.Center, onDismissRequest = onDismiss) {
        DynamicAsyncImage(
            avatarUrl,
            modifier = Modifier,
        )
    }
}
