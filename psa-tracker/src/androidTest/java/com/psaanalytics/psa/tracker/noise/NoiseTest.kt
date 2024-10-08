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
package com.psaanalytics.psa.tracker.noise

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.psaanalytics.core.constants.Parameters
import com.psaanalytics.psa.emitter.BufferOption
import com.psaanalytics.psa.tracker.DevicePlatform
import com.psaanalytics.core.constants.TrackerConstants
import com.psaanalytics.core.emitter.Executor
import com.psaanalytics.core.session.FileStore
import com.psaanalytics.core.tracker.Logger
import com.psaanalytics.core.utils.Util
import com.psaanalytics.psa.network.HttpMethod
import com.psaanalytics.psa.network.Protocol
import com.psaanalytics.psa.tracker.LogLevel
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
class NoiseTest {
    @Test
    fun testEnum() {
        superficialEnumCodeCoverage(BufferOption::class.java)
        superficialEnumCodeCoverage(HttpMethod::class.java)
        superficialEnumCodeCoverage(Protocol::class.java)
        superficialEnumCodeCoverage(LogLevel::class.java)
        superficialEnumCodeCoverage(DevicePlatform::class.java)
    }

    @Test
    fun testPrivateConstructor() {
        superficialPrivateConstructorCodeCoverage(Logger::class.java)
        superficialPrivateConstructorCodeCoverage(FileStore::class.java)
        superficialPrivateConstructorCodeCoverage(Util::class.java)
        superficialPrivateConstructorCodeCoverage(Executor::class.java)
        superficialPrivateConstructorCodeCoverage(Parameters::class.java)
        superficialPrivateConstructorCodeCoverage(TrackerConstants::class.java)
    }

    companion object {
        private fun superficialEnumCodeCoverage(enumClass: Class<out Enum<*>?>) {
            try {
                for (o in enumClass.getMethod("values").invoke(null) as Array<Any>) {
                    enumClass.getMethod("valueOf", String::class.java).invoke(null, o.toString())
                }
            } catch (e: Throwable) {
                throw RuntimeException(e)
            }
        }

        private fun superficialPrivateConstructorCodeCoverage(privateClass: Class<*>) {
            try {
                val constructor = privateClass.getDeclaredConstructor()
                constructor.isAccessible = true
                constructor.newInstance()
            } catch (e: Throwable) {
                throw RuntimeException(e)
            }
        }
    }
}
