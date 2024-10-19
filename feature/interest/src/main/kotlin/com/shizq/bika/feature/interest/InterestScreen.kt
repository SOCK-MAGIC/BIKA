package com.shizq.bika.feature.interest

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.shizq.bika.core.designsystem.component.BikaLoadingWheel
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

@Composable
fun InterestScreen(
    component: InterestComponent,
    navigationToComicList: (String?) -> Unit,
    navigationToSearch: () -> Unit,
    navigationToRanking: () -> Unit,
) {
    val uiState by component.interestUiState.collectAsState()
    InterestContent(
        uiState = uiState,
        navigationToSearch = navigationToSearch,
        navigationToComicList = navigationToComicList,
        navigationToRanking = navigationToRanking,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InterestContent(
    uiState: InterestsUiState,
    navigationToSearch: () -> Unit,
    navigationToRanking: () -> Unit,
    navigationToComicList: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(navigationToSearch) {
                        Icon(Icons.Rounded.Search, "Search")
                    }
                },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            InterestsUiState.Empty -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { Text("什么都没有") }

            InterestsUiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                BikaLoadingWheel(
                    modifier = modifier.size(200.dp),
                    contentDesc = "Loading data",
                )
            }

            is InterestsUiState.Interests -> LazyVerticalGrid(
                GridCells.Fixed(3),
                modifier = modifier.padding(innerPadding),
            ) {
                item {
                    Image(R.drawable.feature_interest_bika, "推荐") {
                        navigationToComicList("recommend")
                    }
                }
                item {
                    Image(
                        R.drawable.feature_interest_cat_leaderboard,
                        "排行榜",
                        navigationToRanking,
                    )
                }
                item {
                    Image(R.drawable.feature_interest_cat_game, "游戏推荐", {})
                }
                item {
                    Image(R.drawable.feature_interest_cat_love_pica, "哔咔小程序", {})
                }
                item {
                    Image(R.drawable.feature_interest_cat_forum, "留言板", {})
                }
                item {
                    Image(R.drawable.feature_interest_cat_latest, "最近更新") {
                        navigationToComicList(null)
                    }
                }
                item {
                    Image(R.drawable.feature_interest_cat_random, "随机本子") {
                        navigationToComicList("random")
                    }
                }
                items(uiState.interests, key = { it.title }) { item ->
                    val context = LocalContext.current
                    Image(item.imageUrl, item.title, null) {
                        if (item.isWeb) {
                            launchCustomChromeTab(context, item.link.toUri())
                        } else {
                            navigationToComicList(item.title)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Image(
    imageUrl: String,
    title: String,
    contentDescription: String?,
    action: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        DynamicAsyncImage(
            imageUrl,
            contentDescription,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(32.dp))
                .clickable { action() },
        )
        Text(title, fontSize = 14.sp)
    }
}

@Composable
private fun Image(id: Int, title: String, action: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(32.dp))
                .clickable { action() },
        )
        Text(title, fontSize = 14.sp)
    }
}

private fun launchCustomChromeTab(context: Context, uri: Uri) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder().build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()
    customTabsIntent.launchUrl(context, uri)
}

@Preview
@Composable
private fun InterestComponentPreviewLoading() {
    InterestScreen(PreviewInterestComponent(), { _ -> }, {}, {})
}

@Preview
@Composable
private fun InterestComponentPreviewEmpty() {
    InterestScreen(
        PreviewInterestComponent().apply {
            interestUiState.value = InterestsUiState.Empty
        },
        { _ -> },
        {},
        {},
    )
}

@Preview
@Composable
private fun InterestComponentPreview() {
    InterestScreen(
        PreviewInterestComponent()
            .apply {
                interestUiState.value = InterestsUiState.Interests(
                    interests = listOf(
                        Interest(
                            id = "electram",
                            isWeb = false,
                            link = "conceptam",
                            title = "mel",
                            imageUrl = "",
                        ),
                        Interest(
                            id = "litora",
                            isWeb = false,
                            link = "at",
                            title = "ceteros",
                            imageUrl = "",
                        ),
                    ),
                )
            },
        { _ -> },
        {},
        {},
    )
}
