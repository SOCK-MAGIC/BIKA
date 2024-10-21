package com.shizq.bika.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.navigation.DrawerComponent

@Composable
fun DrawerScreen(
    component: DrawerComponent,
    navigationToComicList: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier = modifier) {
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
private fun KKK() {
    DrawerScreenNavigationRow(BikaIcons.Chat, "我的评论") {}
}