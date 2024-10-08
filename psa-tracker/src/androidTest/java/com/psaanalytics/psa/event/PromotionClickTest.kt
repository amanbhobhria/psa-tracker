/*
 * Copyright (c) 2015-present Psa Analytics Ltd. All rights reserved.
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
package com.psaanalytics.psa.event

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.psaanalytics.core.constants.Parameters
import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.ecommerce.EcommerceAction
import com.psaanalytics.psa.ecommerce.entities.PromotionEntity
import com.psaanalytics.psa.ecommerce.events.PromotionClickEvent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class PromotionClickTest {
    @Test
    fun testExpectedForm() {
        val promotion = PromotionEntity(
            "promo_id",
            "name",
            listOf("abc", "def", "xyz"),
            4,
            "creative",
            "banner",
            "top_slot"
        )
        val event = PromotionClickEvent(promotion)

        val map = hashMapOf<String, Any>(
            "schema" to TrackerConstants.SCHEMA_ECOMMERCE_PROMOTION,
            "data" to hashMapOf(
                Parameters.ECOMM_PROMO_ID to "promo_id",
                Parameters.ECOMM_PROMO_NAME to "name",
                Parameters.ECOMM_PROMO_PRODUCT_IDS to listOf("abc", "def", "xyz"),
                Parameters.ECOMM_PROMO_POSITION to 4,
                Parameters.ECOMM_PROMO_CREATIVE_ID to "creative",
                Parameters.ECOMM_PROMO_TYPE to "banner",
                Parameters.ECOMM_PROMO_SLOT to "top_slot"
            )
        )
        
        val data: Map<String, Any?> = event.dataPayload
        Assert.assertNotNull(data)
        Assert.assertEquals(EcommerceAction.promo_click.toString(), data[Parameters.ECOMM_TYPE])
        Assert.assertFalse(data.containsKey(Parameters.ECOMM_NAME))

        val entities = event.entitiesForProcessing
        Assert.assertNotNull(entities)
        Assert.assertEquals(1, entities!!.size)
        Assert.assertEquals(map, entities[0].map)
    }
}
