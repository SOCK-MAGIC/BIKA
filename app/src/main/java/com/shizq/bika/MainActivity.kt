package com.shizq.bika

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.metrics.performance.JankStats
import com.shizq.bika.core.data.util.ErrorMonitor
import com.shizq.bika.core.data.util.NetworkMonitor
import com.shizq.bika.ui.BikaApp
import com.shizq.bika.ui.rememberBikaAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var networkMonitor: NetworkMonitor

    /**
     * Lazily inject [JankStats], which is used to track jank throughout the app.
     */
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @Inject
    lateinit var errorMonitor: ErrorMonitor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val appState = rememberBikaAppState(
                networkMonitor = networkMonitor,
                errorMonitor = errorMonitor,
            )
            MaterialTheme {
                BikaApp(appState)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }
}
