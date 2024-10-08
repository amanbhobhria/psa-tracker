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
package com.psaanalytics.psa.ecommerce

import com.psaanalytics.psa.ecommerce.entities.EcommerceScreenEntity
import com.psaanalytics.psa.ecommerce.entities.EcommerceUserEntity

/**
 * Controller for managing Ecommerce entities.
 */
interface EcommerceController {

    /**
     * Add an ecommerce Screen/Page entity to all subsequent events (not just ecommerce events).
     * @param screen A EcommerceScreenEntity.
     */
    fun setEcommerceScreen(screen: EcommerceScreenEntity)

    /**
     * Add an ecommerce User entity to all subsequent events (not just ecommerce events).
     * @param user A EcommerceUserEntity.
     */
    fun setEcommerceUser(user: EcommerceUserEntity)

    /**
     * Stop adding a Screen/Page entity to events.
     */
    fun removeEcommerceScreen()

    /**
     * Stop adding a User entity to events.
     */
    fun removeEcommerceUser()
}
