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
package com.psaanalytics.psa.ecommerce.events

import com.psaanalytics.core.constants.Parameters
import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.ecommerce.EcommerceAction
import com.psaanalytics.psa.ecommerce.entities.CartEntity
import com.psaanalytics.psa.ecommerce.entities.ProductEntity
import com.psaanalytics.psa.event.AbstractSelfDescribing
import com.psaanalytics.psa.payload.SelfDescribingJson

/**
 * Track a product or products being added to cart.
 *
 * @param products - List of product(s) that were added to the cart.
 * @param cart - State of the cart after this addition.
 */
class AddToCartEvent(
    /**
     * List of product(s) that were added to the cart.
     */
    var products: List<ProductEntity>,

    /**
     * State of the cart after the addition.
     */
    var cart: CartEntity
    ) : AbstractSelfDescribing() {

    /** The event schema */
    override val schema: String
        get() = TrackerConstants.SCHEMA_ECOMMERCE_ACTION
    
    override val dataPayload: Map<String, Any?>
        get() {
            val payload = HashMap<String, Any?>()
            payload[Parameters.ECOMM_TYPE] = EcommerceAction.add_to_cart.toString()
            return payload
        }
    
    override val entitiesForProcessing: List<SelfDescribingJson>?
        get() {
            val entities = mutableListOf<SelfDescribingJson>()
            for (product in products) {
                entities.add(product.entity)
            }
            entities.add(cart.entity)
            return entities
        }
}
