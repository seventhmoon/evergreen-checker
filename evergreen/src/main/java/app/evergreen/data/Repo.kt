// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package app.evergreen.data

import android.content.Context
import android.os.Build.*
import android.util.Log
import androidx.annotation.AnyThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.evergreen.R
import app.evergreen.config.EvergreenConfig
import app.evergreen.config.MoshiAdapters
import app.evergreen.extensions.md5
import app.evergreen.services.HttpClient.httpGet
import app.evergreen.services.log
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Repo {
  private val evergreenConfigLiveData: MutableLiveData<EvergreenConfig> = MutableLiveData()

  private val errorsLiveData: MutableLiveData<FetchError> = MutableLiveData()

  val deviceUniqueId = MODEL

  @get:AnyThread
  val evergreenConfig: LiveData<EvergreenConfig>
    get() = evergreenConfigLiveData

  @get:AnyThread
  val errors: LiveData<FetchError>
    get() = errorsLiveData

  fun refreshFromServer(context: Context) {
    log(TAG, "refreshFromServer")
    CoroutineScope(Dispatchers.Main).launch {
      withContext(Dispatchers.IO) {
        val configUrl = getConfigUrl(context, deviceUniqueId)
        Log.e(TAG, "| $configUrl | $deviceUniqueId |")
        val jsonString = httpGet(configUrl)
        if (jsonString == null) {
          Log.i(TAG, deviceUniqueId)
          // Don’t raise a FetchError, since Evergreen’s Tools can be used successfully even on devices
          // for which we don’t have an active app config available.
          return@withContext
        }

        log(TAG, jsonString)
        val json = try {
          MoshiAdapters.updatablesAdapter.fromJson(jsonString)
        } catch (e: JsonEncodingException) {
          errorsLiveData.postValue(FetchError(deviceUniqueId, e.message ?: context.getString(R.string.unknown)))
          null
        } ?: return@withContext

        evergreenConfigLiveData.postValue(json)
      }
    }
  }

  private const val TAG = "Repo"

  fun getConfigUrl(context: Context, deviceUniqueId: String): String {
    return context.getString(R.string.config_url, deviceUniqueId.md5())
  }
}

data class FetchError(val deviceUniqueId: String, val message: String)
