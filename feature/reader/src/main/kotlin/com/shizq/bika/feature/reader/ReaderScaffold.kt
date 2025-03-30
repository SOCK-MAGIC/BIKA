package com.shizq.bika.feature.reader

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ReaderScaffold(
    visibility:Boolean,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    bottomEnd: @Composable RowScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AnimatedVisibility(
                visible = visibility,
                enter = fadeIn(),
                exit = fadeOut(),
                label = "topBar",
            ) {
                topBar()
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = visibility,
                enter = fadeIn(),
                exit = fadeOut(),
                label = "bottomBar",
            ) {
                bottomBar()
            }
        },
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Box(Modifier.fillMaxSize()) {
                content()
            }
            Box(
                Modifier
                    .align(Alignment.BottomEnd),
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    bottomEnd()
                }
            }
        }
    }
}
