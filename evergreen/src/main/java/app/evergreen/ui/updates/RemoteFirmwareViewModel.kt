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

package app.evergreen.ui.updates

import android.content.Context
import app.evergreen.R
import app.evergreen.config.Updatable
import app.evergreen.extensions.drawable

class RemoteFirmwareViewModel(context: Context, updatable: Updatable) : AbstractUpdatableViewModel(context, updatable) {
  override val titleText = context.getString(R.string.remote_firmware)

  override suspend fun getIcon() = context.drawable(R.drawable.remote)!!
}
