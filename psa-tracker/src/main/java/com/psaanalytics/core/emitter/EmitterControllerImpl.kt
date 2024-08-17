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
package com.psaanalytics.core.emitter

import androidx.annotation.RestrictTo
import com.psaanalytics.core.Controller
import com.psaanalytics.core.tracker.Logger
import com.psaanalytics.core.tracker.ServiceProviderInterface
import com.psaanalytics.psa.configuration.EmitterConfiguration
import com.psaanalytics.psa.controller.EmitterController
import com.psaanalytics.psa.emitter.BufferOption
import com.psaanalytics.psa.emitter.EventStore
import com.psaanalytics.psa.network.RequestCallback
import kotlin.time.Duration

@RestrictTo(RestrictTo.Scope.LIBRARY)
class EmitterControllerImpl(serviceProvider: ServiceProviderInterface) :
    Controller(serviceProvider), EmitterController {
    
    private val emitter: Emitter
        get() = serviceProvider.getOrMakeTracker().emitter
    
    override val eventStore: EventStore?
        get() = emitter.eventStore

    override var bufferOption: BufferOption
        get() = emitter.bufferOption
        set(bufferOption) {
            dirtyConfig.bufferOption = bufferOption
            emitter.bufferOption = bufferOption
        }

    override var emitRange: Int
        get() = emitter.emitRange
        set(emitRange) {
            dirtyConfig.emitRange = emitRange
            emitter.emitRange = emitRange
        }

    override val threadPoolSize: Int
        get() = Executor.threadCount
    
    override var byteLimitGet: Long
        get() = emitter.byteLimitGet
        set(byteLimitGet) {
            dirtyConfig.byteLimitGet = byteLimitGet
            emitter.byteLimitGet = byteLimitGet
        }
    
    override var byteLimitPost: Long
        get() = emitter.byteLimitPost
        set(byteLimitPost) {
            dirtyConfig.byteLimitPost = byteLimitPost
            emitter.byteLimitPost = byteLimitPost
        }

    override var requestCallback: RequestCallback?
        get() = emitter.requestCallback
        set(requestCallback) {
            dirtyConfig.requestCallback = requestCallback
            emitter.requestCallback = requestCallback
        }
    
    override var customRetryForStatusCodes: Map<Int, Boolean>?
        get() = emitter.customRetryForStatusCodes
        set(customRetryForStatusCodes) {
            dirtyConfig.customRetryForStatusCodes = customRetryForStatusCodes
            emitter.customRetryForStatusCodes = customRetryForStatusCodes
        }

    override var serverAnonymisation: Boolean
        get() = emitter.serverAnonymisation
        set(serverAnonymisation) {
            dirtyConfig.serverAnonymisation = serverAnonymisation
            emitter.serverAnonymisation = serverAnonymisation
        }

    override var retryFailedRequests: Boolean
        get() = emitter.retryFailedRequests
        set(value) {
            dirtyConfig.retryFailedRequests = value
            emitter.retryFailedRequests = value
        }

    override var maxEventStoreAge: Duration
        get() = emitter.maxEventStoreAge
        set(value) {
            dirtyConfig.maxEventStoreAge = value
            emitter.maxEventStoreAge = value
        }

    override var maxEventStoreSize: Long
        get() = emitter.maxEventStoreSize
        set(value) {
            dirtyConfig.maxEventStoreSize = value
            emitter.maxEventStoreSize = value
        }

    override val dbCount: Long
        get() {
            val eventStore = emitter.eventStore
            if (eventStore == null) {
                Logger.e(TAG, "EventStore not available in the Emitter.")
                return -1
            }
            return eventStore.size()
        }
    
    override val isSending: Boolean
        get() = emitter.emitterStatus

    override fun pause() {
        dirtyConfig.isPaused = true
        emitter.pauseEmit()
    }

    override fun resume() {
        dirtyConfig.isPaused = false
        emitter.resumeEmit()
    }

    // Private methods
    private val dirtyConfig: EmitterConfiguration
        get() = serviceProvider.emitterConfiguration

    companion object {
        private val TAG = EmitterControllerImpl::class.java.simpleName
    }
}
