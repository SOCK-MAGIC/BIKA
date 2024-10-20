package com.shizq.bika.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.navigation.DrawerComponent

@Composable
fun DrawerScreen(component: DrawerComponent, modifier: Modifier = Modifier) {
    ModalDrawerSheet {
        DrawerScreenNavigationRow(BikaIcons.History, "历史记录") {}
        DrawerScreenNavigationRow(BikaIcons.Bookmarks, "我的收藏") {}
        DrawerScreenNavigationRow(BikaIcons.Mail, "我的消息") {}
        DrawerScreenNavigationRow(BikaIcons.Chat, "我的评论") {}
    }
}

@Composable
fun DrawerScreenNavigationRow(imageVector: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector, text)
        Text(text)
    }
}
