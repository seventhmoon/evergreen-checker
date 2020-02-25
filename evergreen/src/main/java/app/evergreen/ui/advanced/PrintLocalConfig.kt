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

package app.evergreen.ui.advanced

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import app.evergreen.R
import app.evergreen.config.Kind
import app.evergreen.config.MoshiAdapters
import app.evergreen.config.Updatable
import app.evergreen.config.Version
import app.evergreen.data.Repo
import app.evergreen.extensions.toast
import app.evergreen.services.log

class PrintLocalConfig(private val context: Context) : AdvancedItem() {
  fun print() {
    Repo.evergreenConfig.observeForever { evergreenConfig ->
      evergreenConfig.updatables.forEach { updatable ->
        getInstalledVersion(updatable)?.let { installedVersion ->
          updatable.latestProd = Version(installedVersion)
        }
      }

      val json = MoshiAdapters.updatablesAdapter.toJson(evergreenConfig)
      log(TAG, json)
      context.toast(R.string.completed_successfully)
    }
  }

  private fun getInstalledVersion(updatable: Updatable): String? = when (updatable.kind) {
    Kind.APK -> {
      try {
        @Suppress("DEPRECATION")
        context.packageManager.getPackageInfo(updatable.id!!, 0).versionName
      } catch (e: PackageManager.NameNotFoundException) {
        null  // App not installed, so it does not have a version name yet.
      }
    }
    Kind.SYSTEM_BUILD -> Build.DISPLAY
    Kind.REMOTE_FIRMWARE -> null
  }

  companion object {
    private const val TAG = "PrintLatestLocalConfig"
  }
}
