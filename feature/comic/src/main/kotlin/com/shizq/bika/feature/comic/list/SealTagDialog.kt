package com.shizq.bika.feature.comic.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    /**
     * usePlatformDefaultWidth = false is use as a temporary fix to allow
     * height recalculation during recomposition. This, however, causes
     * Dialog's to occupy full width in Compact mode. Therefore max width
     * is configured below. This should be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "封印",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsTagPanel()
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = "确定",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
private fun ColumnScope.SettingsTagPanel() {
    Column(Modifier.selectableGroup()) {
        SealTag.categoriesState.forEach { (name, state) ->
            SettingsDialogChooserRow(name, state) {
                SealTag.categoriesState[name] = it
            }
        }
    }
}

@Composable
fun SettingsDialogChooserRow(
    text: String,
    checked: Boolean,
    onClick: (Boolean) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                onValueChange = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Preview
@Composable
private fun PreviewSettingsDialog() {
    SettingsDialog(
        onDismiss = {},
    )
}

@Preview
@Composable
private fun PreviewSettingsDialogLoading() {
    SettingsDialog(
        onDismiss = {},
    )
}

internal object SealTag {
    val categoriesState = mutableStateMapOf(
        "全彩" to false,
        "長篇" to false,
        "同人" to false,
        "短篇" to false,
        "圓神領域" to false,
        "碧藍幻想" to false,
        "CG雜圖" to false,
        "英語 ENG" to false,
        "生肉" to false,
        "純愛" to false,
        "百合花園" to false,
        "耽美花園" to false,
        "偽娘哲學" to false,
        "後宮閃光" to false,
        "扶他樂園" to false,
        "單行本" to false,
        "姐姐系" to false,
        "妹妹系" to false,
        "SM" to false,
        "性轉換" to false,
        "足の恋" to false,
        "人妻" to false,
        "NTR" to false,
        "強暴" to false,
        "非人類" to false,
        "艦隊收藏" to false,
        "Love Live" to false,
        "SAO 刀劍神域" to false,
        "Fate" to false,
        "東方" to false,
        "WEBTOON" to false,
        "禁書目錄" to false,
        "歐美" to false,
        "Cosplay" to false,
        "重口地帶" to false,
    )
    val hideCategoriesFlow = snapshotFlow { hideCategories }
    val hideCategories: List<String>
        get() {
            val result = mutableListOf<String>()
            categoriesState.forEach { (k, v) ->
                if (v) {
                    result.add(k)
                }
            }
            return result
        }
}
