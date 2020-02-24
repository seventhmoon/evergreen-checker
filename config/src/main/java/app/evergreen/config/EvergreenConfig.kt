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

package app.evergreen.config

import app.evergreen.config.Kind.APK
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class EvergreenConfig(
  val updatables: List<Updatable>
)

@JsonClass(generateAdapter = true)
data class Updatable(
  val kind: Kind = APK,
  val latestAlpha: Version? = null,
  val latestBeta: Version? = null,
  val latestProd: Version? = null,
  /** Nullable because Kind.OTA and Kind.REMOTE_FIRMWARE do not need an ID. */
  val id: String? = null
)

enum class Kind {
  APK,
  SYSTEM_BUILD,
  REMOTE_FIRMWARE
}

@JsonClass(generateAdapter = true)
data class Version(
  val versionName: String? = null,
  val releaseDate: Date? = null,
  val minSdkVersion: Int? = null
)
