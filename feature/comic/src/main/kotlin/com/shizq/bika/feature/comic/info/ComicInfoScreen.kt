package com.shizq.bika.feature.comic.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.ui.comicCardItems
import com.webtoonscorp.android.readmore.material3.ReadMoreText

@Composable
fun ComicInfoScreen(
    component: ComicInfoComponent,
    navigationToReader: (String, Int) -> Unit,
    navigationToComicList: (Comics) -> Unit,
    navigationToComment: (String) -> Unit,
    navigationToComicInfo: (String) -> Unit,
) {
    val comicInfoUiState by component.comicInfoUiState.collectAsStateWithLifecycle()
    val epUiState by component.epUiState.collectAsStateWithLifecycle()
    ComicInfoContent(
        uiState = comicInfoUiState,
        epUiState = epUiState,
        navigationToReader = navigationToReader,
        onClickTrigger = component::onClickTrigger,
        onSwitchLike = component::onSwitchLike,
        onSwitchFavorite = component::onSwitchFavorite,
        navigationToComicList = navigationToComicList,
        navigationToComment = navigationToComment,
        navigationToComicInfo = navigationToComicInfo,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ComicInfoContent(
    uiState: ComicInfoUiState,
    epUiState: EpUiState,
    onSwitchLike: () -> Unit,
    onSwitchFavorite: () -> Unit,
    onClickTrigger: (ComicResource) -> Unit,
    navigationToReader: (String, Int) -> Unit,
    navigationToComicList: (Comics) -> Unit,
    navigationToComment: (String) -> Unit,
    navigationToComicInfo: (String) -> Unit,
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
                            IconToggleButton(uiState.toolItem.isFavourite, { onSwitchFavorite() }) {
                                Icon(BikaIcons.Collections, "收藏")
                            }
                        },
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            navigationToReader(uiState.comicResource.id, 1)
                            onClickTrigger(uiState.comicResource)
                        },
                        modifier = Modifier,
                    ) {
                        Text("开始阅读", fontSize = 16.sp)
                    }
                },
                modifier = modifier,
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                ) {
                    item {
                        Info(
                            uiState.comicResource.imageUrl,
                            title = uiState.comicResource.title,
                            author = uiState.comicResource.author,
                            translator = uiState.chineseTeam,
                            total = uiState.totalViews,
                            uiState.comicResource.categories,
                            navigationToComicList,
                            modifier = Modifier,
                        )
                    }
                    item {
                        ToolBar(
                            uiState.toolItem,
                            modifier = Modifier.padding(vertical = 8.dp),
                            onSwitchLike,
                            { navigationToComment(uiState.comicResource.id) },
                        )
                    }
                    item {
                        Creator(
                            uiState.creator,
                            uiState.updatedAt,
                            navigationToComicList = navigationToComicList,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                    item {
                        var expand by remember { mutableStateOf(false) }
                        ReadMoreText(
                            uiState.description,
                            expand,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                ) { expand = !expand },
                        )
                    }
                    item { Tags(uiState.tags) { navigationToComicList(Comics(tag = it)) } }
                    when (epUiState) {
                        EpUiState.Error -> Unit
                        EpUiState.Loading -> Unit
                        is EpUiState.Success -> epBody(epUiState.eps) {
                            navigationToReader(uiState.comicResource.id, it)
                        }
                    }
                    item {
                        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                            items(uiState.bottomRecommend, key = { it.imageUrl }) {
                                Surface(modifier = Modifier.padding(8.dp)) {
                                    DynamicAsyncImage(
                                        it.imageUrl,
                                        null,
                                        Modifier
                                            .size(120.dp, 180.dp)
                                            .clickable { navigationToComicInfo(it.id) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.epBody(eps: List<List<EpDoc>>, onClick: (Int) -> Unit) {
    items(eps) { docs ->
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (doc in docs) {
                TextButton(
                    onClick = { onClick(doc.order) },
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Text(doc.title)
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
            Text("${item.totalLikes}人喜欢", maxLines = 1)
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
    categories: List<String>,
    navigationToComicList: (Comics) -> Unit,
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
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { navigationToComicList(Comics(author = author)) },
            )
            Text(
                translator,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { navigationToComicList(Comics(chineseTeam = translator)) },
            )
            Text("绅士指名次数：$total")
            Text("分类：${categories.fastJoinToString(" ")}")
        }
    }
}

@Composable
private fun Creator(
    creator: Creator,
    updatedAt: String,
    navigationToComicList: (Comics) -> Unit,
    modifier: Modifier = Modifier,
) {
    var show by remember { mutableStateOf(false) }
    if (show) {
        UserDialog(
            creator.avatarUrl,
            creator.gender,
            creator.level.toString(),
            creator.name,
            creator.slogan,
        ) { show = false }
    }
    ElevatedCard(modifier = modifier) {
        ListItem(
            leadingContent = {
                DynamicAsyncImage(
                    creator.avatarUrl,
                    "avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { show = true },
                )
            },
            headlineContent = {
                Text(
                    creator.name,
                    modifier = Modifier.clickable { navigationToComicList(Comics(creatorId = creator.id)) },
                )
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
    FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        categories.fastForEach {
            SuggestionChip(onClick = { onClick(it) }, label = { Text(it) })
        }
    }
}
