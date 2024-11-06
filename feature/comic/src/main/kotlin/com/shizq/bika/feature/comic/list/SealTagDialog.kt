package com.shizq.bika.feature.comic.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.shizq.bika.core.designsystem.component.Dialog
import com.shizq.bika.core.designsystem.component.DialogToggleRow

@Composable
fun SettingsDialog(
    categoryVisibilityUiState: Map<String, Boolean>,
    onChangeCategoryState: (String, Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    Dialog(
        title = {
            Text(
                text = "封印",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 80.dp),
        onDismiss = onDismiss,
    ) {
        HorizontalDivider()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column(Modifier.selectableGroup()) {
                categoryVisibilityUiState.forEach { (name, state) ->
                    DialogToggleRow(name, state) {
                        onChangeCategoryState(name, it)
                    }
                }
            }
            HorizontalDivider(Modifier.padding(top = 8.dp))
        }
    }
}
