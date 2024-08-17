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
package com.psaanalytics.core.globalcontexts

import androidx.annotation.RestrictTo
import com.psaanalytics.core.Controller
import com.psaanalytics.core.tracker.ServiceProviderInterface
import com.psaanalytics.psa.controller.GlobalContextsController
import com.psaanalytics.psa.globalcontexts.GlobalContext

@RestrictTo(RestrictTo.Scope.LIBRARY)
class GlobalContextsControllerImpl(serviceProvider: ServiceProviderInterface) :
    Controller(serviceProvider), GlobalContextsController {

    override val tags: Set<String?>
        get() {
            return serviceProvider.pluginConfigurations.filter {
                it is GlobalContextPluginConfiguration
            }
                .map { it.identifier }
                .toSet()
        }

    override fun add(tag: String, contextGenerator: GlobalContext): Boolean {
        if (tags.contains(tag)) {
            return false
        }
        val plugin = GlobalContextPluginConfiguration(
            identifier = tag,
            globalContext = contextGenerator
        )
        serviceProvider.pluginsController.addPlugin(plugin)
        return true
    }

    override fun remove(tag: String): GlobalContext? {
        val configuration = serviceProvider.pluginConfigurations.firstOrNull {
            it.identifier == tag && it is GlobalContextPluginConfiguration
        } as GlobalContextPluginConfiguration?
        serviceProvider.pluginsController.removePlugin(tag)
        return configuration?.globalContext
    }
}
