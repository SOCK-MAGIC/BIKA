package com.shizq.bika

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import com.arkivanov.decompose.defaultComponentContext
import com.shizq.bika.core.data.util.NetworkMonitor
import com.shizq.bika.navigation.RootComponent
import com.shizq.bika.ui.BikaApp
import com.shizq.bika.ui.rememberBikaAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var rootComponentFactory: RootComponent.Factory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val componentContext = defaultComponentContext()
            val component = rootComponentFactory(componentContext)
            val appState = rememberBikaAppState(networkMonitor = networkMonitor)
            MaterialTheme {
                BikaApp(component, appState)
            }
        }
    }
}
