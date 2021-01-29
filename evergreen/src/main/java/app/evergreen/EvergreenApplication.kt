package app.evergreen

import android.app.Application
import app.evergreen.services.AppServices

class EvergreenApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    AppServices.initLater(applicationContext)
  }
}
