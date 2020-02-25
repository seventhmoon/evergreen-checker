package app.evergreen.ui.advanced

import android.view.ViewGroup
import androidx.leanback.widget.BaseCardView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import app.evergreen.R
import app.evergreen.extensions.color

open class AdvancedItem

class AdvancedPresenter : Presenter() {
  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return ViewHolder(ImageCardView(parent.context).apply {
      setMainImageDimensions(620, 0)
      isFocusable = true
      isFocusableInTouchMode = true
      cardType = BaseCardView.CARD_TYPE_INFO_UNDER_WITH_EXTRA
      infoVisibility = BaseCardView.CARD_REGION_VISIBLE_ALWAYS
      setBackgroundColor(context.color(R.color.grey_700))
    })
  }

  override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
    val imageCardView: ImageCardView = viewHolder.view as ImageCardView
    val context = viewHolder.view.context
    when (item) {
      is PrintLatestLocalConfig -> {
        imageCardView.apply {
          titleText = context.getString(R.string.print_latest_local_config)
          setOnClickListener { PrintLatestLocalConfig().print(context) }
        }
      }
    }
  }

  override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    // Nothing
  }
}
