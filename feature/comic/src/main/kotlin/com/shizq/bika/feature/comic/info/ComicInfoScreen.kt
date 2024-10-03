package com.shizq.bika.feature.comic.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import java.util.Date

@Composable
fun ComicInfoScreen(component: ComicInfoComponent) {
    val uiState by component.comicInfoUiState.collectAsStateWithLifecycle()
    ComicInfoContent(uiState)
}

@Composable
internal fun ComicInfoContent(
    uiState: ComicInfoUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        ComicInfoUiState.Error,
        ComicInfoUiState.Loading -> Unit

        is ComicInfoUiState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Info(
                    uiState.coverUrl,
                    title = uiState.title,
                    author = uiState.author,
                    translator = uiState.chineseTeam,
                    total = uiState.totalViews,
                    modifier = Modifier,
                )
                Creator(
                    uiState.creator.avatar.imageUrl,
                    uiState.creator.name,
                )
                Categories(uiState.categories)
                Tags(uiState.tags)
            }
        }
    }
}

@Composable
internal fun Info(
    cover: String,
    title: String,
    author: String,
    translator: String,
    total: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        DynamicAsyncImage(cover, "cover", Modifier.size(120.dp, 180.dp))
        Column {
            Text(title)
            Text(author)
            Text(translator)
            Text(total.toString())
        }
    }
}

@Composable
private fun Creator(creatorAvatarUrl: String, creatorName: String, modifier: Modifier = Modifier) {
    ElevatedCard(modifier = modifier.padding(8.dp), onClick = {}) {
        ListItem(
            leadingContent = {
                DynamicAsyncImage(
                    creatorAvatarUrl,
                    "avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
            },
            headlineContent = {
                Text(creatorName)
            },
            supportingContent = {
                Text("更新日期")
            },
            modifier = Modifier
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Categories(categories: List<String>, modifier: Modifier = Modifier) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton({}, enabled = false) {
            Text("分类")
        }
        categories.fastForEach {
            OutlinedButton({}) {
                Text(it)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Tags(tags: List<String>, modifier: Modifier = Modifier) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton({}, enabled = false) {
            Text("标签")
        }
        tags.fastForEach {
            OutlinedButton({}) {
                Text(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Toolbar(modifier: Modifier = Modifier) {
    Row {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Favorite, null)
            Text("1234")
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Link, null)
            Text("1234")
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.AutoMirrored.Filled.Message, null)
            Text("1234")
        }
    }
}

@Composable
private fun Chip(modifier: Modifier = Modifier) {
    Box {
    }
}