package com.shizq.bika.feature.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage

@Composable
fun InterestScreen(component: InterestComponent, navigationToComicList: (String, String) -> Unit) {
    val uiState by component.interestUiState.collectAsStateWithLifecycle()
    InterestContent(uiState = uiState, navigationToComicList = navigationToComicList)
}

@Composable
internal fun InterestContent(
    uiState: InterestsUiState,
    navigationToComicList: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {}) { innerPadding ->
        when (uiState) {
            InterestsUiState.Empty -> Text("什么都没有")
            InterestsUiState.Loading -> Text("加载中")
            is InterestsUiState.Interests -> LazyVerticalGrid(
                GridCells.Fixed(3),
                modifier = modifier.padding(innerPadding)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row {
                        Text("工具栏")
                    }
                }
                item {
                    Icon(R.drawable.feature_interest_bika, "推荐", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_leaderboard, "排行榜", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_game, "游戏推荐", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_love_pica, "哔咔小程序", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_forum, "留言板", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_latest, "最近更新", {})
                }
                item {
                    Icon(R.drawable.feature_interest_cat_random, "随机本子", {})
                }
                items(uiState.interests, key = { it.title }) { item ->
                    Icon(item.imageUrl, item.title, null) {
                        if (item.isWeb) {
                        } else {
                            navigationToComicList("categories", item.title)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Icon(imageUrl: String, title: String, contentDescription: String?, action: () -> Unit) {
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
private fun Icon(id: Int, title: String, action: () -> Unit) {
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

@Preview(showBackground = true)
@Composable
private fun SealToolPreview() {
    var showDialog by remember { mutableStateOf(false) }
    SealTool { showDialog = !showDialog }
    SealDialog {
    }
}

@Preview
@Composable
private fun SealDialogPreview() {
    // SealDialog {  }
    SettingsDialog {}
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "封印",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                HorizontalDivider()
                LazyColumn {
                    items(20) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = true, onCheckedChange = {})
                            Text(it.toString())
                        }
                    }
                }
                HorizontalDivider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = "确定",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SealDialog(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val configuration = LocalConfiguration.current
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
    }
}

@Composable
private fun SealTool(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("封印")
        Image(Icons.Outlined.KeyboardArrowDown, null)
    }
}
