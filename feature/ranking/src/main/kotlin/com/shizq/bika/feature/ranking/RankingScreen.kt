package com.shizq.bika.feature.ranking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.AvatarAsyncImage
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.NetworkUser
import com.shizq.bika.core.ui.comicCardItems

@Composable
fun RankingScreen(
    component: RankingComponent,
    navigationToComicInfo: (String) -> Unit,
    navigationToSearch: (String?) -> Unit,
) {
    val rankingUiState by component.rankingUiState.collectAsStateWithLifecycle()
    RankingContent(
        rankingUiState,
        navigationToComicInfo = navigationToComicInfo,
        navigationToSearch = navigationToSearch,
    )
}

@Composable
internal fun RankingContent(
    uiState: RankUiState,
    navigationToComicInfo: (String) -> Unit,
    navigationToSearch: (String?) -> Unit,
) {
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
                        0 -> TabContent(uiState.h24, navigationToComicInfo)

                        1 -> TabContent(uiState.d7, navigationToComicInfo)

                        2 -> TabContent(uiState.d30, navigationToComicInfo)

                        3 -> KnightTabContent(uiState.second) {
                            navigationToSearch(it)
                        }

                        else -> throw IllegalArgumentException("Unknown parameter $selectedIndex")
                    }
                }
            }
        }
    }
}

@Composable
private fun KnightTabContent(second: List<NetworkUser>, onClick: (String) -> Unit) {
    LazyColumn {
        items(second) { item ->
            ListItem(
                leadingContent = {
                    AvatarAsyncImage(item.avatar.imageUrl, item.character)
                },
                headlineContent = {
                    Text(item.name)
                },
                modifier = Modifier.clickable { onClick(item.id) },
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
