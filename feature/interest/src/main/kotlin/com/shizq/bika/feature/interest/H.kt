package com.shizq.bika.feature.interest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun HideInterestDialog(
    interestVisibilityState: Map<String, Boolean>,
    onChangeInterestVisibility: (String, Boolean) -> Unit,
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
                text = "隐藏兴趣",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            HorizontalDivider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Column(Modifier.selectableGroup()) {
                    interestVisibilityState.forEach { (name, state) ->
                        SettingsDialogChooserRow(name, state) {
                            onChangeInterestVisibility(name, it)
                        }
                    }
                }
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
private fun SettingsDialogChooserRow(
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

val interestVisibility = mutableMapOf(
    "推荐" to true,
    "排行榜" to true,
    "游戏推荐" to true,
    "哔咔小程序" to true,
    "留言板" to true,
    "最近更新" to true,
    "随机本子" to true,
    "援助嗶咔" to true,
    "嗶咔小禮物" to true,
    "小電影" to true,
    "小里番" to true,
    "嗶咔畫廊" to true,
    "嗶咔商店" to true,
    "大家都在看" to true,
    "大濕推薦" to true,
    "那年今天" to true,
    "官方都在看" to true,
    "嗶咔運動" to true,
    "嗶咔漢化" to true,
    "全彩" to true,
    "長篇" to true,
    "同人" to true,
    "短篇" to true,
    "圓神領域" to true,
    "碧藍幻想" to true,
    "CG雜圖" to true,
    "英語 ENG" to true,
    "生肉" to true,
    "純愛" to true,
    "百合花園" to true,
    "耽美花園" to true,
    "偽娘哲學" to true,
    "扶他樂園" to true,
    "單行本" to true,
    "姐姐系" to true,
    "妹妹系" to true,
    "SM" to true,
    "性轉換" to true,
    "足の恋" to true,
    "人妻" to true,
    "NTR" to true,
    "強暴" to true,
    "非人類" to true,
    "艦隊收藏" to true,
    "Love Live" to true,
    "SAO 刀劍神域" to true,
    "Fate" to true,
    "東方" to true,
    "WEBTOON" to true,
    "禁書目錄" to true,
    "歐美" to true,
    "Cosplay" to true,
    "重口地帶" to true,
)

