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

package com.psaanalytics.psa.media.event

import com.psaanalytics.core.media.MediaSchemata
import com.psaanalytics.core.media.event.MediaPlayerUpdatingEvent
import com.psaanalytics.psa.event.AbstractSelfDescribing
import com.psaanalytics.psa.media.entity.MediaPlayerEntity

/**
 * Media player event fired immediately after the browser switches into or out of picture-in-picture mode.
 *
 * @param pictureInPicture Whether the video element is showing picture-in-picture after the change.
 */
class MediaPictureInPictureChangeEvent(
    var pictureInPicture: Boolean
) : AbstractSelfDescribing(), MediaPlayerUpdatingEvent {
    override val schema: String
        get() = MediaSchemata.eventSchema("picture_in_picture_change")

    override val dataPayload: Map<String, Any?>
        get() = mapOf(
            "pictureInPicture" to pictureInPicture
        )

    override fun update(player: MediaPlayerEntity) {
        player.pictureInPicture = pictureInPicture
    }
}
