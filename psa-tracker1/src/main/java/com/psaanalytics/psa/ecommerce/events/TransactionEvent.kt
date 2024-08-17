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

import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.ecommerce.EcommerceAction
import com.psaanalytics.psa.ecommerce.entities.ProductEntity
import com.psaanalytics.psa.ecommerce.entities.TransactionEntity
import com.psaanalytics.psa.event.AbstractSelfDescribing
import com.psaanalytics.psa.payload.SelfDescribingJson

/**
 * Track a transaction event.
 * Entity schema: iglu:com.psaanalytics.psa.ecommerce/transaction/jsonschema/1-0-0
 *
 * @param transaction The TransactionEntity details.
 * @param products The product(s) included in the transaction.
 */
class TransactionEvent @JvmOverloads constructor(
    /**
    * The transaction details.
    */
    var transaction: TransactionEntity,
    
    /**
    * Products in the transaction.
    */
    var products: List<ProductEntity>? = null
) : AbstractSelfDescribing() {

    /** The event schema */
    override val schema: String
        get() = TrackerConstants.SCHEMA_ECOMMERCE_ACTION
    
    override val dataPayload: Map<String, Any?>
        get() {
            val payload = HashMap<String, Any?>()
            payload["type"] = EcommerceAction.transaction.toString()
            return payload
        }

    override val entitiesForProcessing: List<SelfDescribingJson>?
        get() {
            val entities = mutableListOf<SelfDescribingJson>()
            products?.let { 
                for (product in it) {
                    entities.add(product.entity)
                }
            }
            entities.add(transaction.entity)
            return entities
        }
}
