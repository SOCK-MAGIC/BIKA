package com.shizq.bika.feature.ranking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.AvatarAsyncImage
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.NetworkKnight
import com.shizq.bika.core.network.model.NetworkRankingDetail
import com.shizq.bika.core.ui.comicCardItems

@Composable
fun RankingScreen(
    component: RankingComponent,
) {
    val rankingUiState by component.rankingUiState.collectAsStateWithLifecycle()
    RankingContent(rankingUiState)
}

@Composable
internal fun RankingContent(uiState: RankUiState) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold { innerPadding ->
        when (uiState) {
            RankUiState.Failed -> Unit
            RankUiState.Loading -> Unit
            is RankUiState.Success -> {
                Column(Modifier.padding(innerPadding)) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedIndex,
                    ) {
                        BikaTab(selectedIndex == 0, "日榜") { selectedIndex = 0 }
                        BikaTab(selectedIndex == 1, "周榜") { selectedIndex = 1 }
                        BikaTab(selectedIndex == 2, "月榜") { selectedIndex = 2 }
                        BikaTab(selectedIndex == 3, "骑士榜") { selectedIndex = 3 }
                    }
                    when (selectedIndex) {
                        0 -> TabContent(uiState.h24) {}

                        1 -> TabContent(uiState.d7) {}

                        2 -> TabContent(uiState.d30) {}

                        3 -> TabContent(uiState.second) {}

                        else -> throw IllegalArgumentException("Unknown parameter $selectedIndex")
                    }
                }
            }
        }
    }
}

@Composable
private fun TabContent(second: List<NetworkKnight.User>, function: () -> Unit) {
    LazyColumn {
        items(second) { item ->
            ListItem(
                leadingContent = {
                    AvatarAsyncImage( item.avatar.imageUrl,  item.character,)
                },
                headlineContent = {
                    Text(item.name)
                },
            )
        }
    }
}

@Composable
private fun TabContent(resources: List<ComicResource>, onClick: (String) -> Unit) {
    LazyColumn {
        comicCardItems(resources, onComicClick = onClick)
    }
}

@Composable
private fun BikaTab(
    selected: Boolean,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier.padding(8.dp),
        content = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = title)
            }
        },
    )
}

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TextTabs() {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Tab 1", "Tab 2", "Tab 3 with lots of text")
    Column {
        PrimaryTabRow(selectedTabIndex = state) {
            titles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) },
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Text tab ${state + 1} selected",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}