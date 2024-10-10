package com.shizq.bika.feature.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shizq.bika.core.designsystem.component.BikaLoadingWheel
import com.shizq.bika.core.designsystem.component.DynamicAsyncImage
import kotlin.reflect.KFunction1

@Composable
fun InterestScreen(component: InterestComponent, navigationToComicList: (String, String) -> Unit) {
    val uiState by component.interestUiState.collectAsStateWithLifecycle()
    val searchQuery by component.searchQuery.collectAsStateWithLifecycle()
    InterestContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchQueryChanged = component::onSearchQueryChanged,
        onSearchTriggered = component::onSearchTriggered,
        navigationToComicList = navigationToComicList,
    )
}

@Composable
internal fun InterestContent(
    uiState: InterestsUiState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    navigationToComicList: (String, String) -> Unit,
    onSearchTriggered: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { SearchTextField(searchQuery, onSearchQueryChanged, onSearchTriggered) },
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
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row {
                        Text("工具栏")
                    }
                }
                item {
                    Image(R.drawable.feature_interest_bika, "推荐", {})
                }
                item {
                    Image(R.drawable.feature_interest_cat_leaderboard, "排行榜", {})
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
                    Image(R.drawable.feature_interest_cat_latest, "最近更新", {})
                }
                item {
                    Image(R.drawable.feature_interest_cat_random, "随机本子", {})
                }
                items(uiState.interests, key = { it.title }) { item ->
                    Image(item.imageUrl, item.title, null) {
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
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val onSearchExplicitlyTriggered = {
        keyboardController?.hide()
        onSearchTriggered(searchQuery)
    }

    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Clear search text",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        onValueChange = {
            if ("\n" !in it) onSearchQueryChanged(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchExplicitlyTriggered()
                    true
                } else {
                    false
                }
            }
            .testTag("searchTextField"),
        shape = RoundedCornerShape(32.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchExplicitlyTriggered()
            },
        ),
        maxLines = 1,
        singleLine = true,
    )
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

@Preview
@Composable
private fun InterestComponentPreviewLoading() {
    InterestScreen(PreviewInterestComponent()) { _, _ -> }
}

@Preview
@Composable
private fun InterestComponentPreviewEmpty() {
    InterestScreen(
        PreviewInterestComponent().apply {
            interestUiState.value = InterestsUiState.Empty
        },
    ) { _, _ -> }
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
    ) { _, _ -> }
}
