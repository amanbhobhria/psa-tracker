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
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class ConsentWithdrawnTest {
    @Test
    fun testExpectedForm() {
        var event = ConsentWithdrawn(false, "id", "v1.0")
            .documentName("name")
            .documentDescription("description")
        var data: Map<*, *> = event.dataPayload
        Assert.assertNotNull(data)
        Assert.assertEquals(false, data[Parameters.CW_ALL])
        val documents: MutableList<ConsentDocument> = LinkedList()
        documents.add(
            ConsentDocument("withdrawn context id 1", "withdrawn context version 1")
                .documentDescription("withdrawn context desc 1")
                .documentName("withdrawn context name 1")
        )
        documents.add(
            ConsentDocument("withdrawn context id 2", "withdrawn context version 2")
                .documentDescription("withdrawn context desc 2")
                .documentName("withdrawn context name 2")
        )
        event = ConsentWithdrawn(false, "id", "v1.0")
            .documentName("name")
            .documentDescription("description")
            .documents(documents)
        data = event.dataPayload
        Assert.assertNotNull(data)
        Assert.assertEquals(false, data[Parameters.CW_ALL])
    }

    @Test
    fun testBuilderFailures() {
        var exception = false
        try {
            ConsentWithdrawn(false, "", "test")
        } catch (e: Exception) {
            Assert.assertEquals("Document ID cannot be empty", e.message)
            exception = true
        }
        Assert.assertTrue(exception)
        exception = false
        try {
            ConsentWithdrawn(false, "test", "")
        } catch (e: Exception) {
            Assert.assertEquals("Document version cannot be empty", e.message)
            exception = true
        }
        Assert.assertTrue(exception)
    }
}
