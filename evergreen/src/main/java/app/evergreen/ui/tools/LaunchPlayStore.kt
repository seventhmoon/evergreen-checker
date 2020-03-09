package app.evergreen.ui.tools

import android.content.Context
import app.evergreen.services.Opener

class LaunchPlayStore(private val context: Context) : Tool {
  override fun doAction() {
    Opener.openPlayStore(context, context.packageName)
  }
}
