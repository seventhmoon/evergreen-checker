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

import android.net.Uri
import java.math.BigInteger
import java.security.MessageDigest

fun String.md5(): String {
  val md = MessageDigest.getInstance("MD5")
  return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

/**
 * When passing as a Uri, "\n" are not honored, but "<br>" are interpreted as line breaks. This screws with the
 * Uri.encode(), and all content after "=" is truncated.
 */
fun String.encodeForEmail(): String = Uri.encode(replace("=", "%3D"))
