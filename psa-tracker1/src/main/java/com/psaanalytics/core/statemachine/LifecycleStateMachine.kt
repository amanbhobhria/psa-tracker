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

import com.psaanalytics.psa.entity.LifecycleEntity
import com.psaanalytics.psa.event.Background
import com.psaanalytics.psa.event.Event
import com.psaanalytics.psa.event.Foreground
import com.psaanalytics.psa.payload.SelfDescribingJson
import com.psaanalytics.psa.tracker.InspectableEvent

class LifecycleStateMachine : StateMachineInterface {
    /*
     States: Visible, NotVisible
     Events: FG (Foreground), BG (Background)
     Transitions:
      - Visible (BG) NotVisible
      - NotVisible (FG) Visible
     Entity Generation:
      - Visible, NotVisible
     */

    override val identifier: String
        get() = ID
    
    override val subscribedEventSchemasForTransitions: List<String>
        get() = listOf(Background.schema, Foreground.schema)

    override val subscribedEventSchemasForEntitiesGeneration: List<String>
        get() = listOf("*")

    override val subscribedEventSchemasForPayloadUpdating: List<String>
        get() = emptyList()

    override val subscribedEventSchemasForAfterTrackCallback: List<String>
        get() = emptyList()

    override val subscribedEventSchemasForFiltering: List<String>
        get() = emptyList()

    override val subscribedEventSchemasForEventsBefore: List<String>
        get() = emptyList()

    override fun transition(event: Event, currentState: State?): State? {
        if (event is Foreground) {
            return LifecycleState(true, event.foregroundIndex)
        }
        if (event is Background) {
            return LifecycleState(false, event.backgroundIndex)
        }
        return null
    }

    override fun entities(event: InspectableEvent, state: State?): List<SelfDescribingJson>? {
        if (state == null) return listOf<SelfDescribingJson>(LifecycleEntity(true))
        
        val s = state as? LifecycleState
        return s?.let { listOf<SelfDescribingJson>(LifecycleEntity(it.isForeground).index(it.index)) }
    }

    override fun payloadValues(event: InspectableEvent, state: State?): Map<String, Any>? {
        return null
    }

    override fun afterTrack(event: InspectableEvent) {
    }

    override fun filter(event: InspectableEvent, state: State?): Boolean? {
        return null
    }

    override fun eventsBefore(event: Event): List<Event>? {
        return null
    }

    companion object {
        val ID: String
            get() = "Lifecycle"
    }
}
