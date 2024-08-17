/*
 * Copyright (c) 2015-present PSA Analytics Ltd. All rights reserved.
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

package com.psaanalytics.core.media.controller

import com.psaanalytics.core.Controller
import com.psaanalytics.core.tracker.ServiceProvider
import com.psaanalytics.psa.media.configuration.MediaTrackingConfiguration
import com.psaanalytics.psa.media.controller.MediaController
import com.psaanalytics.psa.media.controller.MediaTracking
import com.psaanalytics.psa.media.entity.MediaPlayerEntity

class MediaControllerImpl(
    serviceProvider: ServiceProvider
): Controller(serviceProvider), MediaController {
    private var mediaTrackings = mutableMapOf<String, MediaTrackingImpl>()

    override fun startMediaTracking(id: String, player: MediaPlayerEntity?): MediaTracking {
        val configuration = MediaTrackingConfiguration(id = id, player = player)
        return startMediaTracking(configuration = configuration)
    }

    override fun startMediaTracking(configuration: MediaTrackingConfiguration): MediaTracking {
        val session = if (configuration.session) {
            MediaSessionTracking(
                id = configuration.id,
                pingInterval = configuration.pingInterval,
            )
        } else {
            null
        }

        val pingInterval = if (configuration.pings) {
            MediaPingInterval(
                pingInterval = configuration.pingInterval,
                maxPausedPings = configuration.maxPausedPings,
            )
        } else {
            null
        }

        val mediaTracking = MediaTrackingImpl(
            id = configuration.id,
            tracker = serviceProvider.getOrMakeTrackerController(),
            player = configuration.player,
            session = session,
            pingInterval = pingInterval,
            boundaries = configuration.boundaries,
            captureEvents = configuration.captureEvents,
            customEntities = configuration.entities,
        )

        mediaTrackings[configuration.id] = mediaTracking

        return mediaTracking
    }

    override fun getMediaTracking(id: String): MediaTracking? {
        return mediaTrackings[id]
    }

    override fun endMediaTracking(id: String) {
        mediaTrackings[id]?.end()
        mediaTrackings.remove(id)
    }
}
