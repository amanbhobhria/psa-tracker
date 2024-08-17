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
import com.psaanalytics.core.ecommerce.EcommerceAction
import com.psaanalytics.psa.ecommerce.entities.PromotionEntity
import com.psaanalytics.psa.ecommerce.events.PromotionViewEvent
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class PromotionViewTest {
    @Test
    fun testExpectedForm() {
        val promotion = PromotionEntity("promo_id")
        val event = PromotionViewEvent(promotion)
        val data: Map<String, Any?> = event.dataPayload
        Assert.assertNotNull(data)
        Assert.assertEquals(EcommerceAction.promo_view.toString(), data[Parameters.ECOMM_TYPE])
        Assert.assertFalse(data.containsKey(Parameters.ECOMM_NAME))

        val entities = event.entitiesForProcessing
        Assert.assertNotNull(entities)
        Assert.assertEquals(1, entities!!.size)
    }
}
