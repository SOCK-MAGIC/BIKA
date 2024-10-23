package com.shizq.bika.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

@Composable
fun PopupBox(
    popupWidth: Float,
    popupHeight: Float,
    onClickOutside: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green)
            .zIndex(10F),
        contentAlignment = Alignment.Center,
    ) {
        Popup(
            alignment = Alignment.Center,
            properties = PopupProperties(
                excludeFromSystemGesture = true,
            ),
            onDismissRequest = { onClickOutside() },
        ) {
            Box(
                Modifier
                    .width(popupWidth.dp)
                    .height(popupHeight.dp)
                    .background(Color.White)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center,
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun ContentView() {
    val list = listOf("1", "2", "3")
    var showPopup by rememberSaveable { mutableStateOf(false) }
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .background(Color.Yellow)
                .border(1.dp, Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            list.onEachIndexed { index, item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .clickable {
                            showPopup = true
                            selectedIndex = index
                        }
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    }
    if (showPopup) {
        PopupBox(
            popupWidth = 200F,
            popupHeight = 300F,
            onClickOutside = { showPopup = false },
            content = { Text("hello ${list[selectedIndex]}") },
        )
    }
}