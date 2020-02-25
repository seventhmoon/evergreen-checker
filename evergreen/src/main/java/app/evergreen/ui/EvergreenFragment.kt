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

package app.evergreen.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.ErrorSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import app.evergreen.R
import app.evergreen.config.EvergreenConfig
import app.evergreen.data.Repo
import app.evergreen.extensions.color
import app.evergreen.ui.QrCodeFragment.Companion.EXTRA_TEXT
import app.evergreen.ui.tools.ToolsPresenter
import app.evergreen.ui.tools.PrintLocalConfig
import app.evergreen.ui.tools.ToolsObjectAdapter
import app.evergreen.ui.updates.UpdatesPresenter

class EvergreenFragment : BrowseSupportFragment() {
  private val updatesPresenter =
    UpdatesPresenter { dialogFragment, tag -> dialogFragment.show(fragmentManager, tag) }

  private val rowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(ListRowPresenter())

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    showTitle(true)

    adapter = rowsAdapter
    brandColor = requireContext().color(R.color.grey_900)
    badgeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.evergreen)
  }

  override fun onStart() {
    super.onStart()

    Repo.evergreenConfig.observe(this, Observer<EvergreenConfig> { evergreenConfig ->
      rowsAdapter.clear()
      rowsAdapter.add(
        ListRow(HeaderItem(requireContext().getString(R.string.updates)), object : ObjectAdapter() {
          override fun size() = evergreenConfig.updatables.size
          override fun get(position: Int) = evergreenConfig.updatables[position]
        }.apply<ObjectAdapter> {
          presenterSelector = object : PresenterSelector() {
            override fun getPresenter(item: Any?) = updatesPresenter
          }
        })
      )

      rowsAdapter.add(ListRow(HeaderItem(requireContext().getString(R.string.tools)), ToolsObjectAdapter(requireContext())))
    })

    Repo.errors.observe(this, Observer { fetchError ->
      requireFragmentManager().beginTransaction()
        .replace(android.R.id.content, ErrorSupportFragment().apply {
          val context = this@EvergreenFragment.requireContext()
          imageDrawable = context.getDrawable(R.drawable.evergreen)
          message = fetchError.message
          buttonText = context.getString(R.string.qr_code)
          buttonClickListener = View.OnClickListener {
            QrCodeFragment().apply {
              arguments = Bundle().apply {
                putString(EXTRA_TEXT, fetchError.deviceUniqueId + "\n" + Repo.getConfigUrl(context, fetchError.deviceUniqueId))
              }
            }.show(this@EvergreenFragment.requireFragmentManager(), QrCodeFragment.TAG)
          }
        })
        .commit()
    })
  }
}