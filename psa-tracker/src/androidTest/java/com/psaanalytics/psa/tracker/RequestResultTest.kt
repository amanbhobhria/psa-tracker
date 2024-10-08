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
package com.psaanalytics.psa.tracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.psaanalytics.psa.network.RequestResult
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RequestResultTest {
    @Test
    fun testSuccessfulRequest() {
        val result = RequestResult(200, false, listOf(100L))
        Assert.assertTrue(result.isSuccessful)
        Assert.assertFalse(result.oversize)
        Assert.assertFalse(result.shouldRetry(HashMap(), true))
        Assert.assertEquals(result.eventIds, listOf(100L))
    }

    @Test
    fun testFailedRequest() {
        val result = RequestResult(500, false, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertTrue(result.shouldRetry(HashMap(), true))
    }

    @Test
    fun testOversizedFailedRequest() {
        val result = RequestResult(500, true, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertFalse(result.shouldRetry(HashMap(), true))
    }

    @Test
    fun testFailedRequestWithNoRetryStatus() {
        val result = RequestResult(403, false, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertFalse(result.shouldRetry(HashMap(), true))
    }

    @Test
    fun testFailedRequestWithCustomRetryRules() {
        val customRetry: MutableMap<Int, Boolean> = HashMap()
        customRetry[403] = true
        customRetry[500] = false
        var result = RequestResult(403, false, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertTrue(result.shouldRetry(customRetry, true))
        result = RequestResult(500, false, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertFalse(result.shouldRetry(customRetry, true))
    }

    @Test
    fun testFailedRequestWithDisabledRetry() {
        val result = RequestResult(500, false, ArrayList())
        Assert.assertFalse(result.isSuccessful)
        Assert.assertFalse(result.shouldRetry(HashMap(), false))
    }
}
