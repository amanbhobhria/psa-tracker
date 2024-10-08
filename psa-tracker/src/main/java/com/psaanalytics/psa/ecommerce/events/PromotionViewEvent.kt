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
import com.psaanalytics.psa.ecommerce.entities.PromotionEntity
import com.psaanalytics.psa.event.AbstractSelfDescribing
import com.psaanalytics.psa.payload.SelfDescribingJson

/**
 * Track a promotion view.
 *
 * @param promotion - The promotion viewed.
 */
class PromotionViewEvent(var promotion: PromotionEntity) : AbstractSelfDescribing() {

    /** The event schema. */
    override val schema: String
        get() = TrackerConstants.SCHEMA_ECOMMERCE_ACTION
    
    override val dataPayload: Map<String, Any?>
        get() {
            val payload = HashMap<String, Any?>()
            payload["type"] = EcommerceAction.promo_view.toString()
            return payload
        }

    override val entitiesForProcessing: List<SelfDescribingJson>?
        get() = listOf(promotion.entity)
}
