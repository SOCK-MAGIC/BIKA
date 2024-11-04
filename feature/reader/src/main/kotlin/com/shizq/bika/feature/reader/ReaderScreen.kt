package com.shizq.bika.feature.reader

import android.view.KeyEvent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.shizq.bika.core.designsystem.component.ComicReadingAsyncImage
import com.shizq.bika.core.model.Picture
import com.shizq.bika.feature.reader.ClickControl.Action

@Composable
fun ReaderScreen(
    component: ReaderComponent,
) {
    val items = component.pictureFlow.collectAsLazyPagingItems()
    ReaderContent(items)
}

@Composable
internal fun ReaderContent(
    lazyPagingItems: LazyPagingItems<Picture>,
) {
    val clickControl = rememberClickControl()
    Scaffold(
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
                clickControl.scrollPosition.toString(),
                modifier = Modifier.align(Alignment.BottomCenter),
            )
            // Slider()
        }
    }
}
@Composable
fun VolumeButtonsHandler(
    onVolumeUp: () -> Unit,
    onVolumeDown: () -> Unit
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
