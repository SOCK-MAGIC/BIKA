package com.shizq.bika.feature.interest

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InterestScreen(component: InterestComponent) {
    InterestContent()
}

@Composable
internal fun InterestContent(modifier: Modifier = Modifier) {
    Scaffold {
        LazyVerticalGrid(GridCells.Fixed(3)) {
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
        }
    }
}

@Composable
private fun Icon(id: Int, text: String, navigation: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { navigation() }
    ) {
        Image(
            painterResource(id), null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(32.dp)),
        )
        Text(text, fontSize = 14.sp)
    }
}
