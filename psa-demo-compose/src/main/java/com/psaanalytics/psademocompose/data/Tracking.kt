package com.psaanalytics.psademocompose.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.psaanalytics.psa.Psa
import com.psaanalytics.psa.configuration.EmitterConfiguration
import com.psaanalytics.psa.configuration.NetworkConfiguration
import com.psaanalytics.psa.configuration.TrackerConfiguration
import com.psaanalytics.psa.controller.TrackerController
import com.psaanalytics.psa.emitter.BufferOption
import com.psaanalytics.psa.event.ListItemView
import com.psaanalytics.psa.event.ScreenView
import com.psaanalytics.psa.network.HttpMethod
import com.psaanalytics.psa.payload.SelfDescribingJson
import com.psaanalytics.psa.tracker.LogLevel

object Tracking {
    /**
     * Replace this with the URI of your Psa collector
     * (e.g., Micro, Mini, or BDP).
     */
    private const val collectorEndpoint = "placeholder"
    
    @Composable
    fun setup(namespace: String) : TrackerController {
        val networkConfig = NetworkConfiguration(collectorEndpoint, HttpMethod.POST)
        val trackerConfig = TrackerConfiguration("appID")
            .logLevel(LogLevel.DEBUG)
            .screenViewAutotracking(false)
        val emitterConfig = EmitterConfiguration().bufferOption(BufferOption.Single)

        return Psa.createTracker(
            LocalContext.current, 
            namespace,
            networkConfig,
            trackerConfig,
            emitterConfig
        )
    }

    @Composable
    fun AutoTrackScreenView(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Psa.defaultTracker?.track(ScreenView(destination.route ?: "null"))
        }
    }
    
    @Composable
    fun ManuallyTrackScreenView(screenName: String,
                                entities: List<SelfDescribingJson>? = null,
    ) {
        LaunchedEffect(Unit, block = {
            val event = ScreenView(screenName)
            entities?.let { event.entities(it) }
            Psa.defaultTracker?.track(event)
        })
    }

    @Composable
    fun TrackListItemView(index: Int, itemsCount: Int?) {
        LaunchedEffect(Unit, block = {
            val event = ListItemView(index, itemsCount)
            Psa.defaultTracker?.track(event)
        })
    }
}
