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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.shizq.bika.core.backhandle.BackHandler
import com.shizq.bika.core.context.findActivity
import com.shizq.bika.core.designsystem.component.BikaLoadingWheel
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons

@Composable
fun InterestScreen(
    component: InterestComponent,
    navigationToComicList: (String?) -> Unit,
    navigationToSearch: (String?) -> Unit,
    navigationToRanking: () -> Unit,
    openDrawer: () -> Unit,
) {
    val uiState by component.interestUiState.collectAsState()
    val topicsUiState by component.topicsUiState.collectAsState()
    var showSubscriptionDialog by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    BackHandler(component.backHandler) {
        if (showSubscriptionDialog) {
            showSubscriptionDialog = false
            return@BackHandler
        }
        context.findActivity()?.finish()
    }
    InterestContent(
        interestsUiState = uiState,
        navigationToSearch = navigationToSearch,
        navigationToComicList = navigationToComicList,
        navigationToRanking = navigationToRanking,
        onDismissed = { showSubscriptionDialog = false },
        onTopAppBarActionClickSubscriptions = { showSubscriptionDialog = true },
        showSubscriptionDialog = showSubscriptionDialog,
        topicsUiState = topicsUiState,
        updateTopicSelection = component::updateTopicSelection,
        openDrawer = openDrawer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InterestContent(
    interestsUiState: InterestsUiState,
    navigationToSearch: (String?) -> Unit,
    navigationToRanking: () -> Unit,
    navigationToComicList: (String?) -> Unit,
    modifier: Modifier = Modifier,
    onDismissed: () -> Unit,
    onTopAppBarActionClickSubscriptions: () -> Unit,
    showSubscriptionDialog: Boolean,
    topicsUiState: TopicsUiState,
    updateTopicSelection: (String, Boolean) -> Unit,
    openDrawer: () -> Unit,
) {
    if (showSubscriptionDialog) {
        SubscriptionDialog(
            topicsUiState,
            onChangeTopicSelection = updateTopicSelection,
            onDismissed,
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                {},
                actions = {
                    IconButton(onTopAppBarActionClickSubscriptions) {
                        Icon(BikaIcons.Subscriptions, "Subscriptions")
                    }
                    IconButton({ navigationToSearch(null) }) {
                        Icon(BikaIcons.Search, "Search")
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
        when (interestsUiState) {
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
                items(interestsUiState.interests, key = { it.title }) { item ->
                    val context = LocalContext.current
                    Image(item.model, item.title, item.title) {
                        if (item.isWeb) {
                            launchCustomChromeTab(context, item.link.toUri())
                        } else {
                            navigationRouter(
                                item.title,
                                navigationToRanking,
                                navigationToComicList,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun Image(
    imageUrl: Any,
    title: String,
    contentDescription: String?,
    action: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (imageUrl) {
            is Int -> Image(
                painterResource(imageUrl),
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .clickable { action() },
            )

            is String ->
                DynamicAsyncImage(
                    imageUrl,
                    contentDescription,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable { action() },
                )
        }
        Text(title, fontSize = 14.sp)
    }
}

private fun navigationRouter(
    title: String,
    navigationToRanking: () -> Unit,
    navigationToComicList: (String?) -> Unit,
) {
    when (title) {
        "推荐" -> navigationToComicList("recommend")
        "排行榜" -> navigationToRanking()
        "游戏推荐" -> {}
        "哔咔小程序" -> {}
        "留言板" -> {}
        "最近更新" -> navigationToComicList(null)
        "随机本子" -> navigationToComicList("random")
        else -> navigationToComicList(title)
    }
}

private fun launchCustomChromeTab(context: Context, uri: Uri) {
    val customTabBarColor = CustomTabColorSchemeParams.Builder().build()
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(customTabBarColor)
        .build()
    customTabsIntent.launchUrl(context, uri)
}
