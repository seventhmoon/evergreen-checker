package app.evergreen.ui.advanced

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import app.evergreen.config.*
import app.evergreen.data.Repo
import app.evergreen.extensions.toast
import app.evergreen.services.log
import app.evergreen.ui.updates.ApkViewModel

class PrintLatestLocalConfig(private val context: Context) : AdvancedItem() {
  fun print() {
    Repo.evergreenConfig.observeForever { evergreenConfig ->
      evergreenConfig.updatables.forEach { updatable ->
        getInstalledVersion(updatable)?.let { installedVersion ->
          updatable.latestProd?.versionName?.let { availableVersion ->
            if (installedVersion > availableVersion) {
              updatable.latestProd = Version(installedVersion)
            }
          }
        }
      }

      val json = MoshiAdapters.updatablesAdapter.toJson(evergreenConfig)
      log(TAG, json)
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
