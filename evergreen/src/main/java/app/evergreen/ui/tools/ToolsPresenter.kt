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

package app.evergreen.ui.tools

import android.content.Context
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.leanback.widget.*
import app.evergreen.R
import app.evergreen.extensions.color
import app.evergreen.extensions.toTargetSize
import app.evergreen.ui.updates.MAIN_IMAGE_SIZE_DP

class ToolsObjectAdapter(private val context: Context) : ObjectAdapter() {
  init {
    presenterSelector = object : PresenterSelector() {
      override fun getPresenter(item: Any?) = ToolsPresenter()
    }
  }

  override fun size() = 2

  override fun get(position: Int): Tool = when (position) {
    0 -> PrintLocalConfig(context)
    1 -> LaunchPlayStore(context)
    else -> throw UnsupportedOperationException()
  }
}

private class ToolsPresenter : Presenter() {
  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
    return ViewHolder(ImageCardView(parent.context).apply {
      setMainImageDimensions(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
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
      is PrintLocalConfig -> {
        imageCardView.apply {
          titleText = context.getString(R.string.print_local_config)
          mainImage = context.getDrawable(R.drawable.ic_code_json)!!
            .toBitmap(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
            .toTargetSize(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
            .toDrawable(context.resources)
          setOnClickListener { PrintLocalConfig(context).doAction() }
        }
      }
      is LaunchPlayStore -> {
        imageCardView.apply {
          titleText = context.getString(R.string.play_store)
          mainImage = context.getDrawable(R.drawable.google_play)!!
            .toBitmap(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
            .toTargetSize(MAIN_IMAGE_SIZE_DP, MAIN_IMAGE_SIZE_DP)
            .toDrawable(context.resources)
          setOnClickListener { LaunchPlayStore(context).doAction() }
        }
      }
    }
  }

  override fun onUnbindViewHolder(viewHolder: ViewHolder) {
    // Nothing
  }
}

interface Tool {
  fun doAction()
}
