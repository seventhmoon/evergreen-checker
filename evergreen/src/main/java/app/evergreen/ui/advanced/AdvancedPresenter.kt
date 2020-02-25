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
          setOnClickListener { PrintLatestLocalConfig(context).print() }
        }
      }
    }
  }

  override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    // Nothing
  }
}
