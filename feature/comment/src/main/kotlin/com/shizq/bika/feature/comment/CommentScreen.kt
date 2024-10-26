package com.shizq.bika.feature.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons

@Composable
fun CommentScreen(component: CommentComponent) {
    CommentContent()
}

@Composable
internal fun CommentContent(
    modifier: Modifier = Modifier,
) {
}

@Preview
@Composable
private fun KKK() {
    Surface {
        Column {
            ListItem(
                modifier = Modifier
                    .height(IntrinsicSize.Min),
                headlineContent = {
                    Text("用户名")
                },
                supportingContent = {
                    Column {
                        Text("性别 Lv.2 ${System.currentTimeMillis()}")
                    }
                },
                leadingContent = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        DynamicAsyncImage("", modifier = Modifier.size(56.dp))
                    }
                },
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("#120 / 昨天 00:39")
                Spacer(Modifier.weight(1f))
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text("1200")
                    IconToggleButton(true, {}) {
                        Icon(BikaIcons.Favorite, "点赞")
                    }
                }
                Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
                    Text("1200")
                    IconToggleButton(false, {}) {
                        Icon(BikaIcons.Favorite, "点赞")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCommentScreen() {
    CommentScreen(PreviewCommentComponent())
}
