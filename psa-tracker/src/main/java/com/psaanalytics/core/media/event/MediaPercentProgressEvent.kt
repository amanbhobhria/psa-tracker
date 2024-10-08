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

package com.psaanalytics.core.media.event

import com.psaanalytics.core.media.MediaSchemata
import com.psaanalytics.psa.event.AbstractSelfDescribing

/**
 * Media player event fired when a percentage boundary set in the `boundaries` list in `MediaTrackingConfiguration` is reached.
 */
class MediaPercentProgressEvent(val percentProgress: Int?) : AbstractSelfDescribing() {
    override val schema: String
        get() = MediaSchemata.eventSchema("percent_progress")

    override val dataPayload: Map<String, Any?>
        get() = mapOf(
            "percentProgress" to percentProgress,
        )
}
