package com.shizq.bika.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.AvatarAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.navigation.DrawerComponent
import com.shizq.bika.navigation.DrawerUiState
import kotlin.math.pow

@Composable
fun DrawerScreen(
    component: DrawerComponent,
    navigationToComicList: (String?) -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
) {
    val uiState by component.uiState.collectAsStateWithLifecycle()
    ModalDrawerSheet(drawerState, modifier) {
        Column(modifier = Modifier.padding(horizontal = 16.dp).width(IntrinsicSize.Max)) {
            (uiState as? DrawerUiState.Success)?.let { ProfileContent(it) }
            HorizontalDivider(modifier = Modifier)
            DrawerScreenNavigationRow(BikaIcons.History, "最近观看") {
                navigationToComicList("recently")
            }
            DrawerScreenNavigationRow(BikaIcons.Bookmarks, "我的收藏") {
                navigationToComicList("favourite")
            }
            DrawerScreenNavigationRow(BikaIcons.Mail, "我的消息") {}
            DrawerScreenNavigationRow(BikaIcons.Chat, "我的评论") {}
        }
    }
}

@Composable
fun ProfileContent(uiState: DrawerUiState.Success) {
    Column {
        AvatarAsyncImage(
            uiState.netWorkUser.avatar.imageUrl,
            uiState.netWorkUser.character,
            modifier = Modifier.size(64.dp),
        )
        Text("${uiState.netWorkUser.gender} LV.${uiState.netWorkUser.level}(${uiState.netWorkUser.exp}/${exp(uiState.netWorkUser.level)})")
        Text(uiState.netWorkUser.name)
        Text(uiState.netWorkUser.title)
        Text(uiState.netWorkUser.slogan.ifEmpty { "这个人很神秘，什么都没有写" })
    }
}

private fun exp(i: Int) = 100 * (i.toDouble().pow(2) + i).toInt()

@Composable
fun DrawerScreenNavigationRow(imageVector: ImageVector, text: String, onClick: () -> Unit) {
    Button(onClick) {
        Icon(imageVector, text)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text)
    }
}

@Preview
@Composable
private fun DrawerScreenNavigationRowPreview() {
    DrawerScreenNavigationRow(BikaIcons.Chat, "我的评论") {}
}
