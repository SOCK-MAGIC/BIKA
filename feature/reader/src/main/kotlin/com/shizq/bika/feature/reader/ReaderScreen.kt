package com.shizq.bika.feature.reader

import android.view.KeyEvent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.ComicReadingAsyncImage
import com.shizq.bika.core.model.Picture
import com.shizq.bika.feature.reader.ClickControl.Action

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(component: ReaderComponent) {
    val items = component.pictureFlow.collectAsLazyPagingItems()
    val clickControl = rememberClickControl()
    // todo rememberSaveable save impl
    val sliderState = remember(clickControl.scrollPosition, component.pageCount) {
        SliderState(
            value = clickControl.scrollPosition.toFloat(),
            valueRange = 1.toFloat()..component.pageCount,
        )
    }

    ReaderContent(
        lazyPagingItems = items,
        clickControl = clickControl,
        showControllerOptions = clickControl.showControllerOptions,
        onDismissRequest = { clickControl.showControllerOptions = false },
        sliderState = sliderState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReaderContent(
    lazyPagingItems: LazyPagingItems<Picture>,
    clickControl: ClickControl,
    showControllerOptions: Boolean,
    onDismissRequest: () -> Unit,
    sliderState: SliderState,
) {
    Scaffold(
        topBar = {},
        bottomBar = {},
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    clickControl.click(it)
                }
            }
            .fillMaxSize(),
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .onKeyEvent {
                    when (it.key) {
                        Key.VolumeUp -> {
                            clickControl.click(Action.NEXT)
                            true
                        }

                        Key.VolumeDown -> {
                            clickControl.click(Action.NEXT)
                            true
                        }

                        else -> false
                    }
                }
                .fillMaxSize(),
        ) {
            LazyColumn(state = clickControl.lazyListState, modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems.itemCount, key = { it }) { index ->
                    lazyPagingItems[index]?.let {
                        ComicReadingAsyncImage(it.url, modifier = Modifier.fillMaxSize())
                    }
                }
            }
            Text(
                "第一章",
                modifier = Modifier.align(Alignment.BottomStart),
            )
            Text(
                clickControl.scrollPosition.toString() + "/100",
                modifier = Modifier.align(Alignment.BottomEnd),
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
