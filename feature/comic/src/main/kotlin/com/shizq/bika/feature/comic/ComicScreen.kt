package com.shizq.bika.feature.comic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ComicScreen(component: ComicListComponent) {
    ComicContent()
}

@Composable
internal fun ComicContent(modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
private fun ComicCard() {
    Card {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Image(
                Icons.Default.Link,
                null,
                Modifier.size(120.dp, 180.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .height(180.dp)
                    .padding(8.dp)
            ) {
                Text("title", style = MaterialTheme.typography.titleLarge)
                Text("1E305p", style = MaterialTheme.typography.labelSmall)
                Text(
                    "author",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
                Text("分类", style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.weight(1f))
                Row {
                    Text("1234", modifier = Modifier.wrapContentHeight())
                }
            }
        }
    }
}
