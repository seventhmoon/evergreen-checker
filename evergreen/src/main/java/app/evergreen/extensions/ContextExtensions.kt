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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.evergreen.BuildConfig
import app.evergreen.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Context.toast(message: String) {
  if (BuildConfig.DEBUG) {
    Log.e("Toast", message)
  }
  if (Thread.currentThread() === Looper.getMainLooper().thread) {
    // Toasts can only be shown from the main thread.
    Toast.makeText(this.applicationContext, message, Toast.LENGTH_LONG).show()
  } else {
    val context = this
    CoroutineScope(Dispatchers.Main).launch {
      Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
    }
  }
}

fun Context.toast(@StringRes message: Int) = toast(getString(message))

fun Context.safeStartActivity(intent: Intent): Boolean {
  return try {
    startActivity(intent)
    true
  } catch (e: ActivityNotFoundException) {
    toast(R.string.error_no_app_to_handle_this_action)
    false
  } catch (e: SecurityException) {
    toast(e.localizedMessage ?: e.message ?: getString(R.string.unknown))
    false
  }
}

fun Context.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Context.drawable(@DrawableRes drawableRes: Int): Drawable? =
  ResourcesCompat.getDrawable(resources, drawableRes, null)
