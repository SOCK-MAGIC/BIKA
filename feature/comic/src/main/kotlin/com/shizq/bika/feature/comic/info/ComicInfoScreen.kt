package com.shizq.bika.feature.comic.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.network.model.NetworkComicInfo
import kotlin.reflect.KFunction1

@Composable
fun ComicInfoScreen(
    component: ComicInfoComponent,
    navigationToReader: (String) -> Unit,
    navigationToComicList: (Comics) -> Unit,
) {
    val uiState by component.comicInfoUiState.collectAsStateWithLifecycle()
    ComicInfoContent(
        uiState = uiState,
        navigationToReader = navigationToReader,
        onClickTrigger = component::onClickTrigger,
        onSwitchLike = component::onSwitchLike,
        onSwitchFavorite = component::onSwitchFavorite,
        navigationToComicList = navigationToComicList,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComicInfoContent(
    uiState: ComicInfoUiState,
    navigationToReader: (String) -> Unit,
    onClickTrigger: (ComicResource) -> Unit,
    onSwitchLike: () -> Unit,
    onSwitchFavorite: () -> Unit,
    navigationToComicList: (Comics) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ComicInfoUiState.Error, ComicInfoUiState.Loading -> Unit

        is ComicInfoUiState.Success -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        {},
                        navigationIcon = {},
                        actions = {
                            IconButton(onSwitchFavorite) {
                                Icon(
                                    BikaIcons.Collections,
                                    "收藏",
                                    tint = if (uiState.toolItem.isFavourite) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.secondary
                                    },
                                )
                            }
                        },
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            navigationToReader(uiState.comicResource.id)
                            onClickTrigger(uiState.comicResource)
                        },
                        modifier = Modifier,
                    ) {
                        Text("开始阅读", fontSize = 16.sp)
                    }
                },
                modifier = modifier,
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .verticalScroll(state = rememberScrollState())
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize(),
                ) {
                    Info(
                        uiState.comicResource.imageUrl,
                        title = uiState.comicResource.title,
                        author = uiState.comicResource.author,
                        translator = uiState.chineseTeam,
                        total = uiState.totalViews,
                        modifier = Modifier,
                    )
                    ToolBar(
                        uiState.toolItem,
                        modifier = Modifier.padding(vertical = 8.dp),
                        onSwitchLike,
                        {},
                    )
                    Creator(
                        uiState.creator,
                        uiState.updatedAt,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                    Text(uiState.description, modifier = Modifier.padding(vertical = 8.dp))
                    Tags(uiState.tags) { navigationToComicList(Comics(tag = it)) }
                    Tags(uiState.comicResource.categories) { navigationToComicList(Comics(category = it)) }
                }
            }
        }
    }
}

@Composable
fun ToolBar(
    item: ToolItem,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ToolBarItem(
            Modifier
                .weight(1f)
                .clickable { onLikeClick() },
        ) {
            Icon(
                BikaIcons.Favorite,
                "喜欢",
                tint = if (item.isLiked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.secondary
                },
            )
            Text("${item.totalLikes}人喜欢")
        }
        VerticalDivider(Modifier.padding(8.dp))
        ToolBarItem(
            Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            Text("${item.pagesCount}页")
            Text("${item.epsCount}章")
        }
        VerticalDivider(Modifier.padding(8.dp))
        ToolBarItem(
            Modifier
                .weight(1f)
                .clickable { onCommentClick() },
        ) {
            Icon(BikaIcons.Chat, "评论")
            Text("${item.commentsCount}条评论")
        }
    }
}

@Composable
fun ToolBarItem(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
internal fun Info(
    cover: String,
    title: String,
    author: String,
    translator: String,
    total: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        DynamicAsyncImage(cover, "cover", Modifier.size(120.dp, 180.dp))
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                author,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                translator,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(total.toString())
        }
    }
}

@Composable
private fun Creator(
    creator: NetworkComicInfo.Comic.Creator,
    updatedAt: String,
    modifier: Modifier = Modifier,
) {
    var show by remember { mutableStateOf(false) }
    if (show) {
        UserDialog(
            creator.avatar.fileServer,
            creator.gender,
            creator.level.toString(),
            creator.name,
            creator.slogan,
        ) { show = false }
    }
    ElevatedCard(
        modifier = modifier,
        onClick = { show = true },
    ) {
        ListItem(
            leadingContent = {
                DynamicAsyncImage(
                    creator.avatar.fileServer,
                    "avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                )
            },
            headlineContent = {
                Text(creator.name)
            },
            supportingContent = {
                Text(updatedAt)
            },
            modifier = Modifier,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tags(categories: List<String>, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        categories.fastForEach {
            SuggestionChip(onClick = { onClick(it) }, label = { Text(it) })
        }
    }
}
