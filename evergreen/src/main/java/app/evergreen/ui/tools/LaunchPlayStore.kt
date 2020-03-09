package app.evergreen.ui.tools

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import app.evergreen.R
import app.evergreen.extensions.toTargetSize
import app.evergreen.services.Opener
import app.evergreen.ui.MAIN_IMAGE_SIZE_DP

class LaunchPlayStore(private val context: Context) : Tool {
  override val titleText: String
    get() = context.getString(R.string.play_store)

  override val mainImage: Drawable
    get() = context.getDrawable(R.drawable.google_play)!!
      .toBitmap(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
      .toTargetSize(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
      .toDrawable(context.resources)

  override fun doAction() {
    Opener.openPlayStore(context, context.packageName)
  }
}
