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

package com.psaanalytics.psa.util

import java.util.*
import kotlin.time.Duration

class TimeTraveler {
    private var date: Date = Date()

    fun travelBy(duration: Duration) {
        date = Date(date.time + duration.inWholeMilliseconds)
    }

    fun generateDate(): Date {
        return date
    }

    fun generateTimestamp(): Long {
        return date.time
    }
}
