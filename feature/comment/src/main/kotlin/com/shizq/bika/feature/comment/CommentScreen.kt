package com.shizq.bika.feature.comment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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
private fun PreviewCommentScreen() {
    CommentScreen(PreviewCommentComponent())
}
