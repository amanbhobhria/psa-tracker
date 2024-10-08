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
package com.psaanalytics.core.ecommerce

import androidx.annotation.RestrictTo
import com.psaanalytics.core.tracker.ServiceProviderInterface
import com.psaanalytics.psa.configuration.PluginConfiguration
import com.psaanalytics.psa.ecommerce.EcommerceController
import com.psaanalytics.psa.ecommerce.entities.EcommerceScreenEntity
import com.psaanalytics.psa.ecommerce.entities.EcommerceUserEntity

@RestrictTo(RestrictTo.Scope.LIBRARY)
class EcommerceControllerImpl(val serviceProvider: ServiceProviderInterface) : EcommerceController {

    override fun setEcommerceScreen(screen: EcommerceScreenEntity) {
        val plugin = PluginConfiguration("ecommercePageTypePluginInternal")
        plugin.entities { listOf(screen.entity) }
        serviceProvider.addPlugin(plugin)
    }

    override fun setEcommerceUser(user: EcommerceUserEntity) {
        val plugin = PluginConfiguration("ecommerceUserPluginInternal")
        plugin.entities { listOf(user.entity) }
        serviceProvider.addPlugin(plugin)
    }

    override fun removeEcommerceScreen() {
        serviceProvider.removePlugin("ecommercePageTypePluginInternal")
    }

    override fun removeEcommerceUser() {
        serviceProvider.removePlugin("ecommerceUserPluginInternal")
    }
}
