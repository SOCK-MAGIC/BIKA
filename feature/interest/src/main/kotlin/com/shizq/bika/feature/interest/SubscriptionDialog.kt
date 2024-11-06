package com.shizq.bika.feature.interest

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
fun SubscriptionDialog(
    topicsUiState: TopicsUiState,
    onChangeTopicSelection: (String, Boolean) -> Unit,
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    Dialog(
        title = {
            Text(
                text = "隐藏兴趣",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        onDismiss,
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 80.dp),
    ) {
        HorizontalDivider()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            when (topicsUiState) {
                TopicsUiState.Loading -> Text(
                    text = "Loading…",
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                is TopicsUiState.Success -> Column(Modifier.selectableGroup()) {
                    topicsUiState.topics.forEach { (name, state) ->
                        DialogToggleRow(name, state) {
                            onChangeTopicSelection(name, it)
                        }
                    }
                }
            }
            HorizontalDivider(Modifier.padding(top = 8.dp))
        }
    }
}
