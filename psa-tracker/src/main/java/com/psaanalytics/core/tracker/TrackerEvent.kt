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
package com.psaanalytics.core.tracker

import com.psaanalytics.core.constants.Parameters
import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.statemachine.StateMachineEvent
import com.psaanalytics.core.statemachine.TrackerState
import com.psaanalytics.core.statemachine.TrackerStateSnapshot
import com.psaanalytics.psa.event.AbstractPrimitive
import com.psaanalytics.psa.event.AbstractSelfDescribing
import com.psaanalytics.psa.event.Event
import com.psaanalytics.psa.event.TrackerError
import com.psaanalytics.psa.payload.Payload
import com.psaanalytics.psa.payload.SelfDescribingJson
import java.util.*

class TrackerEvent @JvmOverloads constructor(event: Event, state: TrackerStateSnapshot? = null) :
    StateMachineEvent {
    
    override var schema: String? = null
    override var name: String? = null
    override lateinit var payload: MutableMap<String, Any>
    override lateinit var state: TrackerStateSnapshot
    override lateinit var entities: MutableList<SelfDescribingJson>

    var eventId: UUID = UUID.randomUUID()
    var timestamp: Long = System.currentTimeMillis()
    var trueTimestamp: Long?
    var isPrimitive = false
    var isService: Boolean

    init {
        entities = event.entities.toMutableList()
        trueTimestamp = event.trueTimestamp
        payload = HashMap(event.dataPayload)
        
        if (state != null) {
            this.state = state
        } else {
            this.state = TrackerState()
        }
        
        isService = event is TrackerError
        if (event is AbstractPrimitive) {
            name = event.name
            isPrimitive = true
        } else {
            schema = (event as? AbstractSelfDescribing)?.schema
            isPrimitive = false
        }
    }

    override fun addPayloadValues(payload: Map<String, Any>): Boolean {
        var result = true
        for ((key, value) in payload) {
            if (this.payload[key] == null) {
                this.payload[key] = value
            } else {
                result = false
            }
        }
        return result
    }

    fun addContextEntity(entity: SelfDescribingJson) {
        entities.add(entity)
    }

    fun wrapEntitiesToPayload(payload: Payload, base64Encoded: Boolean) {
        if (entities.isEmpty()) {
            return
        }

        val data: MutableList<Map<String, Any?>> = LinkedList()
        for (entity in entities) {
            data.add(entity.map)
        }
        val finalContext = SelfDescribingJson(TrackerConstants.SCHEMA_CONTEXTS, data)
        payload.addMap(
            finalContext.map,
            base64Encoded,
            Parameters.CONTEXT_ENCODED,
            Parameters.CONTEXT
        )
    }

    fun wrapPropertiesToPayload(toPayload: Payload, base64Encoded: Boolean) {
        if (isPrimitive) {
            toPayload.addMap(payload)
        } else {
            wrapSelfDescribingToPayload(toPayload, base64Encoded)
        }
    }

    private fun wrapSelfDescribingToPayload(toPayload: Payload, base64Encoded: Boolean) {
        val schema = schema ?: return
        val data = SelfDescribingJson(schema, payload)
        val unstructuredEventPayload = HashMap<String?, Any?>()
        unstructuredEventPayload[Parameters.SCHEMA] = TrackerConstants.SCHEMA_UNSTRUCT_EVENT
        unstructuredEventPayload[Parameters.DATA] = data.map
        toPayload.addMap(
            unstructuredEventPayload,
            base64Encoded,
            Parameters.UNSTRUCTURED_ENCODED,
            Parameters.UNSTRUCTURED
        )
    }
}
