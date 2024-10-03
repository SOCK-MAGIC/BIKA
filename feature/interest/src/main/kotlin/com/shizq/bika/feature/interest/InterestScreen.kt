package com.shizq.bika.feature.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Scaffold { innerPadding ->
        when (uiState) {
            InterestsUiState.Empty -> Text("什么都没有")
            InterestsUiState.Loading -> Text("加载中")
            is InterestsUiState.Interests -> LazyVerticalGrid(
                GridCells.Fixed(3),
                modifier = modifier.padding(innerPadding)
            ) {
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
