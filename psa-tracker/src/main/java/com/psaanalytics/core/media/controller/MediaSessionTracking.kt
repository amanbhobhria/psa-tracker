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

import com.psaanalytics.core.media.entity.MediaSessionEntity
import com.psaanalytics.psa.event.Event
import com.psaanalytics.psa.media.entity.MediaAdBreakEntity
import com.psaanalytics.psa.media.entity.MediaPlayerEntity
import com.psaanalytics.psa.payload.SelfDescribingJson
import java.util.*

class MediaSessionTracking(
    id: String,
    startedAt: Date? = null,
    pingInterval: Int? = null,
    dateGenerator: () -> Date = { Date() }
) {
    var session: MediaSessionEntity
    var stats: MediaSessionTrackingStats

    val entity: SelfDescribingJson
        get() = session.entity(stats)

    init {
        session = MediaSessionEntity(id = id, startedAt = startedAt ?: Date(), pingInterval = pingInterval)
        stats = MediaSessionTrackingStats(session = session, dateGenerator = dateGenerator)
    }

    fun update(event: Event?, player: MediaPlayerEntity, adBreak: MediaAdBreakEntity?) {
        stats.update(event, player, adBreak)
    }
}
