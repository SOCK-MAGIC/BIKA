package com.shizq.bika.feature.comic.info

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
