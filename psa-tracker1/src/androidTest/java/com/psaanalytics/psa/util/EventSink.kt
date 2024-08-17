package com.psaanalytics.psa.util

import com.psaanalytics.psa.configuration.*
import com.psaanalytics.psa.tracker.InspectableEvent

class EventSink : Configuration, PluginIdentifiable, PluginFilterCallable {

    var trackedEvents = mutableListOf<InspectableEvent>()

    override val identifier: String
        get() = "EventSink"

    override val filterConfiguration: PluginFilterConfiguration?
        get() = PluginFilterConfiguration { event ->
            trackedEvents.add(event)
            false
        }

    override fun copy(): Configuration {
        TODO("Not yet implemented")
    }

}
