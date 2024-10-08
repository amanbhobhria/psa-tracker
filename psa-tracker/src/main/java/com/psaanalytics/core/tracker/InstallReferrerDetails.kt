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

package com.psaanalytics.core.tracker

import android.content.Context
import android.os.RemoteException
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.ReferrerDetails
import com.psaanalytics.core.utils.Util
import com.psaanalytics.psa.payload.SelfDescribingJson

/**
 * Entity tracked along with the `application_install` event to give information about the Play Store referrer.
 * Only tracked in case `com.android.installreferrer` package is added to app dependencies.
 * 
 * Schema: iglu:com.android.installreferrer.api/referrer_details/jsonschema/1-0-0
 *
 * @param installReferrer The referrer URL of the installed package
 * @param referrerClickTimestamp The timestamp when referrer click happens
 * @param installBeginTimestamp The timestamp when the app installation began
 * @param googlePlayInstantParam Boolean indicating if the user has interacted with the app's instant experience in the past 7 days
 */
class InstallReferrerDetails(
    installReferrer: String,
    referrerClickTimestamp: Long,
    installBeginTimestamp: Long,
    googlePlayInstantParam: Boolean
) : SelfDescribingJson(
    "iglu:com.android.installreferrer.api/referrer_details/jsonschema/1-0-0",
    mapOf(
        "installReferrer" to installReferrer,
        "referrerClickTimestamp" to if (referrerClickTimestamp > 0) { Util.getDateTimeFromTimestamp(referrerClickTimestamp * 1000) } else { null },
        "installBeginTimestamp" to if (installBeginTimestamp > 0) { Util.getDateTimeFromTimestamp(installBeginTimestamp * 1000) } else { null },
        "googlePlayInstantParam" to googlePlayInstantParam,
    )
) {
    companion object {
        private val TAG = InstallReferrerDetails::class.java.simpleName

        fun fetch(context: Context, callback: (installReferrer: InstallReferrerDetails?) -> Unit) {
            if (!isInstallReferrerPackageAvailable()) {
                callback(null)
                return
            }

            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerResponse.OK -> {
                            // Connection established.
                            try {
                                val response: ReferrerDetails = referrerClient.installReferrer
                                val referrer = InstallReferrerDetails(
                                    installReferrer = response.installReferrer,
                                    referrerClickTimestamp = response.referrerClickTimestampSeconds,
                                    installBeginTimestamp = response.installBeginTimestampSeconds,
                                    googlePlayInstantParam = response.googlePlayInstantParam
                                )
                                callback(referrer)
                            } catch (_: RemoteException) {
                                Logger.d(TAG, "Install referrer API remote exception.")
                                callback(null)
                            } catch (_: NoSuchMethodError) {
                                Logger.d(TAG, "Upgrade InstallReferrer package to v1.1 or higher to add the install referrer details entity.")
                                callback(null)
                            }
                        }
                        InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            Logger.d(
                                TAG,
                                "Install referrer API not available on the current Play Store app."
                            )
                            callback(null)
                        }
                        InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            Logger.d(
                                TAG,
                                "Install referrer API connection couldn't be established."
                            )
                            callback(null)
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }

        private fun isInstallReferrerPackageAvailable(): Boolean {
            try {
                Class.forName("com.android.installreferrer.api.InstallReferrerStateListener")
                return true
            } catch (_: Exception) {
            }
            return false
        }
    }
}
