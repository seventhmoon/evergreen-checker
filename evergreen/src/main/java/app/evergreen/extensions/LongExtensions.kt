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

package app.evergreen.extensions

import java.text.StringCharacterIterator

/** https://stackoverflow.com/a/3758880/131884 */
fun Long.humanReadableByteCountSI(): String? {
  if (-1000 < this && this < 1000) {
    return "$this B"
  }
  var mutableBytes = this
  val characterIterator = StringCharacterIterator("kMGTPE")
  while (mutableBytes <= -999950 || mutableBytes >= 999950) {
    mutableBytes /= 1000
    characterIterator.next()
  }
  return java.lang.String.format("%.1f %cB", mutableBytes / 1000.0, characterIterator.current())
}

