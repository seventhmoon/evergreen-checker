package app.evergreen.services

import android.content.Context
import app.evergreen.data.Repo

object AppServices {
  private lateinit var context: Context
  fun initLater(context: Context) {
    this.context = context
  }

  val repo by lazy { Repo(context) }
  val httpClient by lazy { HttpClient() }
  val opener by lazy { Opener(context) }
  val systemProp by lazy { SystemProp() }
}
