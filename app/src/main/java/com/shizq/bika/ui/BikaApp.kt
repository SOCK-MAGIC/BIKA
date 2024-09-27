package com.shizq.bika.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.Placeholder
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.shizq.bika.core.designsystem.component.BikaNavigationSuiteScaffold
import com.shizq.bika.feature.interest.InterestScreen
import com.shizq.bika.feature.signin.SignInScreen
import com.shizq.bika.navigation.RootComponent

@Composable
fun BikaApp(
    component: RootComponent,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    BikaApp(component, modifier)
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun BikaApp(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    ModalNavigationDrawer(
        drawerContent = { Text("drawerContent") },
        modifier = modifier,
        drawerState = rememberDrawerState(DrawerValue.Closed),
    ) {
        Scaffold(
            topBar = { SearchBar(inputField = {}, expanded = false, onExpandedChange = {}) { } },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                Box(modifier = Modifier.consumeWindowInsets(WindowInsets(0, 0, 0, 0))) {
                    RootContent(component)
                }
            }
        }
    }
}

@Composable
private fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation { _ -> slide() + fade() },
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.SignIn -> SignInScreen(
                component = child.component,
                component::navigationToInterest
            )

            is RootComponent.Child.Interest -> InterestScreen(component = child.component)
        }
    }
}