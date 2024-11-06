package com.shizq.bika.feature.reader

import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.shizq.bika.core.designsystem.component.ComicReadingAsyncImage
import com.shizq.bika.core.designsystem.icon.BikaIcons
import com.shizq.bika.core.model.Picture

@Composable
fun ReaderScreen(component: ReaderComponent) {
    val items = component.picturePagingFlow.collectAsLazyPagingItems()
    val bottomText by component.bottomText.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    ReaderContent(
        lazyPagingItems = items,
        bottomText = bottomText,
        lazyListState = component.lazyListState,
        showActionMenu = component.showActionMenu,
        onClick = { component.onClick(it, scope) },
        physicalClick = { component.onClick(it, scope) },
    )
}

@Composable
internal fun ReaderContent(
    lazyPagingItems: LazyPagingItems<Picture>,
    lazyListState: LazyListState,
    bottomText: String,
    showActionMenu: Boolean,
    onClick: (Offset) -> Unit,
    physicalClick: (PageScrollingDirection) -> Unit,
) {
    val density = LocalDensity.current
    Scaffold(
        topBar = {},
        bottomBar = {
            AnimatedVisibility(
                showActionMenu,
                enter = slideInVertically {
                    with(density) { 80.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Bottom,
                ) + fadeIn(
                    initialAlpha = 0.3f,
                ),
                exit = slideOutVertically { with(density) { 80.dp.roundToPx() } } + shrinkVertically() + fadeOut(),
            ) {
                BikaBottomBar()
            }
        },
        modifier = Modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures {
                            onClick(it)
                        }
                    }
                    .onKeyEvent {
                        when (it.key) {
                            Key.VolumeUp -> {
                                physicalClick(PageScrollingDirection.NEXT)
                                true
                            }

                            Key.VolumeDown -> {
                                physicalClick(PageScrollingDirection.PREV)
                                true
                            }

                            else -> false
                        }
                    }
                    .fillMaxSize(),
            ) {
                items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) { index ->
                    lazyPagingItems[index]?.let {
                        ComicReadingAsyncImage(it.url, modifier = Modifier.fillMaxSize())
                    }
                }
            }
            Text(
                bottomText,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun BottomOptions() {
    Surface(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
    ) {
        var value by remember { mutableFloatStateOf(0f) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("1/200")
            Slider(
                value,
                onValueChange = {
                    value = it
                },
                valueRange = 1f..200f,
                onValueChangeFinished = {},
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }
    }
}

@Composable
internal fun BikaBottomBar(modifier: Modifier = Modifier) {
    BottomAppBar(modifier, tonalElevation = 1.dp) {
        BikaBottomBarItem {
            Icon(BikaIcons.ScreenRotationAlt, "屏幕方向")
            Text("屏幕方向")
        }
        BikaBottomBarItem {
            if (true) {
                Icon(BikaIcons.SwapVert, "滑动方向")
            } else {
                Icon(BikaIcons.SwapHoriz, "滑动方向")
            }
            Text("滑动方向")
        }
    }
}

@Composable
internal fun RowScope.BikaBottomBarItem(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
    )
}

@Composable
fun VolumeButtonsHandler(
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    DisposableEffect(context) {
        val keyEventDispatcher = ViewCompat.OnUnhandledKeyEventListenerCompat { _, event ->
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    onVolumeUp()
                    true
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    onVolumeDown()
                    true
                }

                else -> {
                    false
                }
            }
        }

        ViewCompat.addOnUnhandledKeyEventListener(view, keyEventDispatcher)

        onDispose {
            ViewCompat.removeOnUnhandledKeyEventListener(view, keyEventDispatcher)
        }
    }
}
