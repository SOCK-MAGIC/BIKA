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
import androidx.compose.material.icons.rounded.Menu
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.shizq.bika.core.designsystem.icon.BikaIcons

@Composable
fun InterestScreen(
    component: InterestComponent,
    navigationToComicList: (String?) -> Unit,
    navigationToSearch: () -> Unit,
    navigationToRanking: () -> Unit,
    openDrawer: () -> Unit,
) {
    val uiState by component.interestUiState.collectAsState()
    val interestVisibilityState by component.interestVisibilityUiState.collectAsState()
    var showHideInterestsDialog by rememberSaveable { mutableStateOf(false) }
    InterestContent(
        uiState = uiState,
        navigationToSearch = navigationToSearch,
        navigationToComicList = navigationToComicList,
        navigationToRanking = navigationToRanking,
        onDismissed = {
            showHideInterestsDialog = false
        },
        onTopAppBarActionClick = { showHideInterestsDialog = true },
        showHideInterestsDialog = showHideInterestsDialog,
        interestVisibilityState = interestVisibilityState,
        onChangeInterestVisibility = component::updateInterestVisibility,
        openDrawer = openDrawer,
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
    onDismissed: () -> Unit,
    onTopAppBarActionClick: () -> Unit,
    showHideInterestsDialog: Boolean,
    interestVisibilityState: Map<String, Boolean>,
    onChangeInterestVisibility: (String, Boolean) -> Unit,
    openDrawer: () -> Unit,
) {
    if (showHideInterestsDialog) {
        HideInterestDialog(
            interestVisibilityState,
            onChangeInterestVisibility = onChangeInterestVisibility,
        ) { onDismissed() }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(onTopAppBarActionClick) {
                        Icon(BikaIcons.VisibilityOff, "Search")
                    }
                    IconButton(navigationToSearch) {
                        Icon(Icons.Rounded.Search, "Search")
                    }
                },
                navigationIcon = {
                    IconButton(openDrawer) {
                        Icon(BikaIcons.Menu, "Open Drawer")
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
                if (interestVisibilityState.getOrDefault("推荐", true)) {
                    item {
                        Image(R.drawable.feature_interest_bika, "推荐") {
                            navigationToComicList("recommend")
                        }
                    }
                }
                if (interestVisibilityState.getOrDefault("排行榜", true)) {
                    item {
                        Image(
                            R.drawable.feature_interest_cat_leaderboard,
                            "排行榜",
                            navigationToRanking,
                        )
                    }
                }
                if (interestVisibilityState.getOrDefault("游戏推荐", true)) {
                    item {
                        Image(R.drawable.feature_interest_cat_game, "游戏推荐", {})
                    }
                }
                if (interestVisibilityState.getOrDefault("哔咔小程序", true)) {
                    item {
                        Image(R.drawable.feature_interest_cat_love_pica, "哔咔小程序", {})
                    }
                }
                if (interestVisibilityState.getOrDefault("留言板", true)) {
                    item {
                        Image(R.drawable.feature_interest_cat_forum, "留言板", {})
                    }
                }
                if (interestVisibilityState.getOrDefault("最近更新", true)) {
                    item {
                        Image(R.drawable.feature_interest_cat_latest, "最近更新") {
                            navigationToComicList(null)
                        }
                    }
                }
                if (interestVisibilityState.getOrDefault("随机本子", true)) {
                    item {
                        Image(R.drawable.feature_interest_cat_random, "随机本子") {
                            navigationToComicList("random")
                        }
                    }
                }
                items(uiState.interests, key = { it.title }) { item ->
                    val context = LocalContext.current
                    Image(item.imageUrl, item.title, item.title) {
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
    InterestScreen(PreviewInterestComponent(), { _ -> }, {}, {}, {})
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
        {},
    )
}
