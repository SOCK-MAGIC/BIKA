package com.shizq.bika.router

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.value.Value

interface NavigationDrawerState<out T> {
    val drawerState: DrawerState
    val instance: T
}

@Composable
fun <T : Any> rememberNavigationDrawerState(
    drawer: Value<ChildDrawer<T>>,
    onStateChanged: (isOpen: Boolean) -> Unit,
): NavigationDrawerState<T> {
    val childDrawer by drawer.subscribeAsState()
    val drawerState = rememberDrawerState(initialValue = if (childDrawer.isOpen) DrawerValue.Open else DrawerValue.Closed)

    DisposableEffect(drawerState.isOpen) {
        onStateChanged(drawerState.isOpen)
        onDispose {}
    }

    LaunchedEffect(childDrawer.isOpen) {
        if (childDrawer.isOpen) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }

    return object : NavigationDrawerState<T> {
        override val drawerState: DrawerState get() = drawerState
        override val instance: T get() = childDrawer.instance
    }
}