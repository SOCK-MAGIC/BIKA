package com.shizq.bika.feature.reader

import android.view.KeyEvent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

@Composable
fun ReaderScreen(component: ReaderComponent) {
    val items = component.picturePagingFlow.collectAsLazyPagingItems()
    val controller = component.controller
    val controllerVisibility by controller.visible.collectAsStateWithLifecycle()
    val progression by controller.progress.collectAsStateWithLifecycle()
    ReaderContent(
        lazyPagingItems = items,
        lazyListState = controller.lazyListState,
        readingProgress = progression,
        readableItems = items.itemCount,
        onChangeReadingProgress = { controller.updateProgress(it) },
        onClick = controller::onClick,
        controllerVisibility = controllerVisibility,
    )
}

@Composable
internal fun ReaderContent(
    lazyPagingItems: LazyPagingItems<Picture>,
    lazyListState: LazyListState,
    controllerVisibility: Boolean,
    onClick: suspend (Offset) -> Unit,
    readingProgress: Float,
    readableItems: Int,
    onChangeReadingProgress: (Float) -> Unit,
) {
    val scope = rememberCoroutineScope()
    ReaderScaffold(
        visibility = controllerVisibility,
        topBar = {
            // BikaTopBar()
        },
        bottomBar = {
            BikaBottomBar(
                readingProgress,
                0f..readableItems.toFloat(),
                onChangeReadingProgress,
            )
        },
        modifier = Modifier
            .fillMaxSize(),
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        scope.launch {
                            onClick(it)
                        }
                    }
                }
                .fillMaxSize(),
        ) {
            items(lazyPagingItems.itemCount, key = lazyPagingItems.itemKey { it.id }) { index ->
                lazyPagingItems[index]?.let {
                    val nextImage =
                        lazyPagingItems.peek((index + 5).coerceAtMost(lazyPagingItems.itemCount - 1))
                    ComicReadingAsyncImage(
                        it.url,
                        nextImage?.url,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BikaTopBar(title: String, onBackPress: () -> Unit, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, tonalElevation = 4.dp) {
        TopAppBar(
            title = {
                Text(title)
            },
            modifier = Modifier,
            navigationIcon = {
                IconButton(onBackPress) {
                    Icon(BikaIcons.ArrowBack, "返回")
                }
                onBackPress()
            },
        )
    }
}

@Composable
internal fun BikaBottomBar(
    index: Float,
    range: ClosedFloatingPointRange<Float>,
    updateTrack: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier, tonalElevation = 4.dp) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            BikaSlider(index, range, updateTrack, modifier = Modifier.weight(3f))
        }
        // BikaBottomBarItem {
        //     Icon(BikaIcons.ScreenRotationAlt, "屏幕方向")
        //     Text("屏幕方向")
        // }
        // BikaBottomBarItem {
        //     if (true) {
        //         Icon(BikaIcons.SwapVert, "滑动方向")
        //     } else {
        //         Icon(BikaIcons.SwapHoriz, "滑动方向")
        //     }
        //     Text("滑动方向")
        // }
    }
}

@Composable
private fun BikaSlider(
    index: Float,
    range: ClosedFloatingPointRange<Float>,
    updateTrack: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        index,
        updateTrack,
        valueRange = range,
        modifier = modifier,
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

@Preview
@Composable
private fun BikaBottomBarPreview() {
    BikaBottomBar(5f, 0f..100f, {})
}

@Preview
@Composable
private fun BikaTopBarPreview() {
    BikaTopBar("第一话 第一话第一话 第一话第一话 第一话第一话 第一话第一话 第一话", {})
}
