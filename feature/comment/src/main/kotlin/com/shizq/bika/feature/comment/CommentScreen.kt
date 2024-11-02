package com.shizq.bika.feature.comment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.data.model.Comment
import com.shizq.bika.core.designsystem.component.AvatarAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons

@Composable
fun CommentScreen(component: CommentComponent) {
    val lazyPagingItems = component.pagingDataFlow.collectAsLazyPagingItems()
    CommentContent(lazyPagingItems)
}

@Composable
internal fun CommentContent(lazyPagingItems: LazyPagingItems<Comment>) {
    Scaffold { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(lazyPagingItems.itemCount) {
                lazyPagingItems[it]?.let {
                    CommentRow(it)
                }
            }
        }
    }
}

@Composable
private fun CommentRow(comment: Comment, modifier: Comment = Modifier) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Row {
                AvatarAsyncImage("", "", modifier = Modifier.size(80.dp))
                Text(
                    "dddddddddddddddddddddddddddddd",
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("#120 / 昨天 00:39")
                Spacer(Modifier.weight(1f))
                Text("1200")
                Icon(BikaIcons.Favorite, "点赞", modifier = Modifier.padding(horizontal = 8.dp))
                Text("1200")
                Icon(BikaIcons.Favorite, "点赞", modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCommentScreen() {
    CommentScreen(PreviewCommentComponent())
}
