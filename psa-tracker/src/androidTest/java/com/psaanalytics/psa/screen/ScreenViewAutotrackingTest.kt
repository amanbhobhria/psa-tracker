/*
 * Copyright (c) 2015-present Psa Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.psaanalytics.psa.tracker

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.emitter.Executor
import com.psaanalytics.core.utils.NotificationCenter
import com.psaanalytics.psa.Psa
import com.psaanalytics.psa.Psa.removeAllTrackers
import com.psaanalytics.psa.configuration.Configuration
import com.psaanalytics.psa.configuration.NetworkConfiguration
import com.psaanalytics.psa.controller.TrackerController
import com.psaanalytics.psa.event.ScreenView
import com.psaanalytics.psa.network.HttpMethod
import com.psaanalytics.psa.util.EventSink
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ScreenViewAutotrackingTest {

    @After
    fun tearDown() {
        removeAllTrackers()
        Executor.shutdown()
    }

    // --- TESTS
    @Test
    fun doesntTrackTheSameScreenViewMultipleTimes() {
        val eventSink = EventSink()
        createTracker(listOf(eventSink))
        Thread.sleep(200)

        NotificationCenter.postNotification("SnowplowScreenView", mapOf(
            "event" to ScreenView(name = "Screen1").activityClassName("Screen1")
        ))
        Thread.sleep(200)

        NotificationCenter.postNotification("SnowplowScreenView", mapOf(
            "event" to ScreenView(name = "Screen1").activityClassName("Screen1")
        ))
        Thread.sleep(200)

        NotificationCenter.postNotification("SnowplowScreenView", mapOf(
            "event" to ScreenView(name = "Screen2").activityClassName("Screen2")
        ))
        Thread.sleep(500)

        val numberOfScreenViews = eventSink.trackedEvents.filter { it.schema == TrackerConstants.SCHEMA_SCREEN_VIEW }.size
        Assert.assertEquals(2, numberOfScreenViews)
    }

    // --- PRIVATE
    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    private fun createTracker(configurations: List<Configuration>): TrackerController {
        val networkConfig = NetworkConfiguration(MockNetworkConnection(HttpMethod.POST, 200))
        return Psa.createTracker(
            context,
            namespace = "testScreenView" + Math.random().toString(),
            network = networkConfig,
            configurations = configurations.toTypedArray()
        )
    }
}
