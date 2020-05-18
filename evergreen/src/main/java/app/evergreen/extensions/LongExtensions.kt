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

