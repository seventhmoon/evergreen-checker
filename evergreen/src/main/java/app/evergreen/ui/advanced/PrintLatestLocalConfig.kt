package app.evergreen.ui.advanced

import android.content.Context
import app.evergreen.extensions.toast

class PrintLatestLocalConfig : AdvancedItem() {
  fun print(context: Context) {
    context.toast("PrintLatestLocalConfig")
  }
}
