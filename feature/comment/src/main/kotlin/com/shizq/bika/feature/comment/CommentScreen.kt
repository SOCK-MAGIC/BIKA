package com.shizq.bika.feature.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.shizq.bika.core.data.model.Comment
import com.shizq.bika.core.designsystem.component.AvatarAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(component: CommentComponent, onBackClick: () -> Unit) {
    val lazyPagingItems = component.pagingDataFlow.collectAsLazyPagingItems()
    val childLazyPagingItems = component.childCommentPagingDataFlow.collectAsLazyPagingItems()
    val bottomSheetState =
        rememberStandardBottomSheetState(SheetValue.Hidden, skipHiddenState = false)
    val scope = rememberCoroutineScope()
    BackHandler(backHandler = component.backHandler) {
        if (bottomSheetState.currentValue == SheetValue.Expanded) {
            scope.launch {
                bottomSheetState.hide()
            }
        } else {
            onBackClick()
        }
    }
    CommentContent(
        lazyPagingItems = lazyPagingItems,
        childLazyPagingItems = childLazyPagingItems,
        bottomSheetState = bottomSheetState,
        updateCommentId = component::updateCommentId,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CommentContent(
    lazyPagingItems: LazyPagingItems<Comment>,
    childLazyPagingItems: LazyPagingItems<Comment>,
    bottomSheetState: SheetState,
    updateCommentId: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    BottomSheetScaffold(
        sheetContent = {
            LazyColumn(Modifier) {
                items(childLazyPagingItems.itemCount) { index ->
                    childLazyPagingItems[index]?.let { comment ->
                        CommentRow(comment) {
                        }
                        HorizontalDivider(Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        },
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = bottomSheetState.currentValue == SheetValue.Expanded,
    ) { paddingValues ->
        Scaffold(Modifier.consumeWindowInsets(paddingValues)) { innerPadding ->
            LazyColumn(Modifier.padding(innerPadding)) {
                items(lazyPagingItems.itemCount) { index ->
                    lazyPagingItems[index]?.let { comment ->
                        CommentRow(comment) {
                            updateCommentId(it)
                            scope.launch { bottomSheetState.expand() }
                        }
                        HorizontalDivider(Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BackHandler(backHandler: BackHandler, isEnabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)

    val callback = remember { BackCallback(isEnabled = isEnabled) { currentOnBack() } }

    SideEffect { callback.isEnabled = isEnabled }

    DisposableEffect(backHandler) {
        backHandler.register(callback)
        onDispose { backHandler.unregister(callback) }
    }
}

@Composable
private fun CommentRow(comment: Comment, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    Surface(modifier = modifier.clickable { onClick(comment.id) }) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(Modifier.padding(bottom = 8.dp)) {
                AvatarAsyncImage(
                    comment.user.avatarUrl,
                    comment.user.character,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Text(comment.user.name, fontWeight = FontWeight.Bold)
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("Lv.${comment.user.level}")
                            }
                            append("\t")
                            withStyle(SpanStyle(background = MaterialTheme.colorScheme.primaryContainer)) {
                                append(comment.user.title)
                            }
                        },
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(comment.content)
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(comment.createdAt)
                Spacer(Modifier.weight(1f))
                Text(comment.likesCount.toString())
                Icon(BikaIcons.Favorite, "点赞", modifier = Modifier.padding(horizontal = 8.dp))
                Text(comment.subComment.toString())
                Icon(BikaIcons.Comment, "评论", modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCommentScreen() {
    CommentScreen(PreviewCommentComponent(), {})
}
