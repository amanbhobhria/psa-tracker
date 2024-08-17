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
package com.psaanalytics.core.statemachine

import com.psaanalytics.psa.configuration.PluginAfterTrackConfiguration
import com.psaanalytics.psa.configuration.PluginEntitiesConfiguration
import com.psaanalytics.psa.configuration.PluginFilterConfiguration
import com.psaanalytics.psa.event.Event
import com.psaanalytics.psa.payload.SelfDescribingJson
import com.psaanalytics.psa.tracker.InspectableEvent
import java.util.*

class PluginStateMachine(
    override val identifier: String,
    val entitiesConfiguration: PluginEntitiesConfiguration?,
    val afterTrackConfiguration: PluginAfterTrackConfiguration?,
    val filterConfiguration: PluginFilterConfiguration?
) : StateMachineInterface {

    override val subscribedEventSchemasForTransitions: List<String>
        get() = emptyList()

    override val subscribedEventSchemasForEntitiesGeneration: List<String>
        get() {
            val config = entitiesConfiguration ?: return emptyList()
            return config.schemas ?: Collections.singletonList("*")
        }

    override val subscribedEventSchemasForPayloadUpdating: List<String>
        get() = emptyList()

    override val subscribedEventSchemasForAfterTrackCallback: List<String>
        get() {
            val config = afterTrackConfiguration ?: return emptyList()
            return config.schemas ?: Collections.singletonList("*")
        }

    override val subscribedEventSchemasForFiltering: List<String>
        get() {
            val config = filterConfiguration ?: return emptyList()
            return config.schemas ?: Collections.singletonList("*")
        }

    override val subscribedEventSchemasForEventsBefore: List<String>
        get() = emptyList()

    override fun transition(event: Event, state: State?): State? {
        return null
    }

    override fun entities(event: InspectableEvent, state: State?): List<SelfDescribingJson> {
        return entitiesConfiguration?.closure?.apply(event) ?: emptyList()
    }

    override fun payloadValues(event: InspectableEvent, state: State?): Map<String, Any>? {
        return null
    }

    override fun afterTrack(event: InspectableEvent) {
        afterTrackConfiguration?.closure?.accept(event)
    }

    override fun filter(event: InspectableEvent, state: State?): Boolean? {
        return filterConfiguration?.closure?.apply(event)
    }

    override fun eventsBefore(event: Event): List<Event>? {
        return null
    }
}
