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
package com.psaanalytics.psa.controller

import com.psaanalytics.psa.configuration.PluginIdentifiable

/**
 * Controller for managing plugins initialized in the tracker.
 */
interface PluginsController {
    /**
     * List of initialized plugin identifiers.
     */
    val identifiers: List<String>

    /**
     * Add a new plugin.
     */
    fun addPlugin(plugin: PluginIdentifiable)

    /**
     * Remove plugin with the identifier.
     */
    fun removePlugin(identifier: String)
}
